package com.dangerfield.podawan

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.consent.ConsentStatus
import com.dangerfield.features.consent.ConsentStatusRepository
import com.dangerfield.features.forcedupdate.IsAppUpdateRequired
import com.dangerfield.features.inAppMessaging.GetInAppUpdateAvailability
import com.dangerfield.features.inAppMessaging.InAppUpdateAvailability
import com.dangerfield.features.inAppMessaging.InstallInAppUpdate
import com.dangerfield.features.inAppMessaging.StartInAppUpdate
import com.dangerfield.features.inAppMessaging.UpdateStatus
import com.dangerfield.libraries.config.AppConfigFlow
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.coreflowroutines.tryWithTimeout
import com.dangerfield.libraries.dictionary.GetDeviceLanguageSupportLevel
import com.dangerfield.libraries.dictionary.LanguageSupportLevel
import com.dangerfield.libraries.dictionary.LanguageSupportMessageShown
import com.dangerfield.libraries.dictionary.ShouldShowLanguageSupportMessage
import com.dangerfield.libraries.session.EnsureSessionLoaded
import com.dangerfield.libraries.session.SessionFlow
import com.dangerfield.libraries.session.UserRepository
import com.dangerfield.podawan.MainActivityViewModel.Action
import com.dangerfield.podawan.MainActivityViewModel.State
import com.dangerfield.podawan.startup.EnsureAppConfigLoaded
import com.dangerfield.podawan.startup.IsInMaintenanceMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import podawan.core.Catching
import podawan.core.doNothing
import podawan.core.failFast
import podawan.core.logOnFailure
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val ensureAppConfigLoaded: EnsureAppConfigLoaded,
    private val ensureSessionLoaded: EnsureSessionLoaded,
    private val isAppUpdateRequired: IsAppUpdateRequired,
    private val consentStatusRepository: ConsentStatusRepository,
    private val sessionFlow: SessionFlow,
    private val getLanguageSupportLevel: GetDeviceLanguageSupportLevel,
    private val shouldShowLanguageSupportMessage: ShouldShowLanguageSupportMessage,
    private val languageSupportMessageShown: LanguageSupportMessageShown,
    private val appConfigFlow: AppConfigFlow,
    private val isInMaintenanceMode: IsInMaintenanceMode,
    private val getInAppUpdateAvailability: GetInAppUpdateAvailability,
    private val startInAppUpdate: StartInAppUpdate,
    private val installInAppUpdate: InstallInAppUpdate,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<State, Unit, Action>(
    savedStateHandle,
    State(
        isUpdateRequired = false,
        isLoadingApp = true,
        hasBlockingError = false,
        languageSupportLevelMessage = null,
        consentStatus = null,
        isInMaintenanceMode = false,
        inAppUpdateStatus = null,
        isLoggedIn = false
    )
) {

    // TODO make a notification manager to handle all these dialogs I may or may not show.
    // in app messages can likely have a handler that takes in a class wrapping the route and stuff
    // and a priority and such.
    private var inAppUpdateJob: Job? = null

    init {
        takeAction(Action.LoadApp)
    }

    override suspend fun handleAction(action: Action) {
        with(action) {
            when (this) {
                is Action.LoadApp -> loadApp()
                is Action.MarkLanguageSupportLevelMessageShown -> languageSupportMessageShown(
                    languageSupportLevel
                )

                is Action.LoadConsentStatus -> listenForConsentStatusUpdates()
                is Action.StartInAppUpdate -> launchInAppUpdate()
                is Action.CompleteInAppUpdate -> handleInstall()
            }
        }
    }

    fun startInAppUpdate(
        activity: Activity,
        updateStatus: UpdateStatus.UpdateAvailable
    ) {
        takeAction(Action.StartInAppUpdate(activity, updateStatus))
    }

    fun installUpdate() {
        takeAction(Action.CompleteInAppUpdate)
    }

    private suspend fun Action.CompleteInAppUpdate.handleInstall() {
        updateState { it.copy(inAppUpdateStatus = UpdateStatus.Installing) }
        installInAppUpdate()
            .onSuccess {
                updateState { it.copy(inAppUpdateStatus = UpdateStatus.Installed) }
            }
            .onFailure { error ->
                updateState { it.copy(inAppUpdateStatus = UpdateStatus.Failed(error)) }
            }
    }

    private fun Action.StartInAppUpdate.launchInAppUpdate() {
        if (inAppUpdateJob?.isActive == true) return
        inAppUpdateJob = viewModelScope.launch {
            startInAppUpdate(
                updateStatus.appUpdateInfo,
                updateStatus.isForegroundUpdate,
                activity
            ).collect { status ->
                updateState { it.copy(inAppUpdateStatus = status) }
            }
        }
    }

    private fun Action.LoadConsentStatus.listenForConsentStatusUpdates() {
        viewModelScope.launch {
            consentStatusRepository.getStatusFlow(activity)
                .collectLatest { status ->
                    updateState { it.copy(consentStatus = status) }
                }
        }
    }

    private suspend fun Action.LoadApp.loadApp() {
        tryWithTimeout(10.seconds) {
            requiredStartupTasks.awaitAll().failFast()
        }
            .logOnFailure()
            .onFailure {
                updateState {
                    it.copy(
                        hasBlockingError = true,
                        isLoadingApp = false,
                        isLoggedIn = userRepository.getUserFlow().first().isLoggedIn
                    )
                }
            }
            .onSuccess {
                updateState {
                    it.copy(
                        isLoadingApp = false,
                        languageSupportLevelMessage = null,
                        isLoggedIn = userRepository.getUserFlow().first().isLoggedIn
                    )
                }

                listenForAppUpdateRequired()
                listenForSessionUpdates()
                getLanguageSupport()
                listenForConfigUpdates()
                checkForAppUpdate()
            }
    }

    private suspend fun Action.LoadApp.checkForAppUpdate() {
        viewModelScope.launch {
            getInAppUpdateAvailability()
                .logOnFailure()
                .onSuccess { availability ->
                    when (availability) {
                        is InAppUpdateAvailability.UpdateAvailable -> {
                            updateState {
                                it.copy(
                                    inAppUpdateStatus = UpdateStatus.UpdateAvailable(
                                        shouldUpdate = availability.shouldShow,
                                        appUpdateInfo = availability.appUpdateInfo,
                                        isForegroundUpdate = availability.isForegroundUpdate
                                    )
                                )
                            }
                        }

                        is InAppUpdateAvailability.NoUpdateAvailable -> {
                            updateState {
                                it.copy(inAppUpdateStatus = UpdateStatus.NoUpdateAvailable)
                            }
                        }

                        is InAppUpdateAvailability.UpdateReadyToInstall -> {
                            updateState {
                                it.copy(
                                    inAppUpdateStatus = UpdateStatus.Downloaded(
                                        wasBackgroundUpdate = availability.wasDownloadedInBackground
                                    )
                                )
                            }
                        }

                        InAppUpdateAvailability.UpdateInProgress -> {
                            // Should be an impossilbe state.
                            doNothing()
                        }
                    }
                }
        }
    }

    private suspend fun Action.LoadApp.getLanguageSupport() {
        viewModelScope.launch {
            val languageSupportLevel = getLanguageSupportLevel()
            val shouldShow = shouldShowLanguageSupportMessage(languageSupportLevel)
            if (shouldShow) {
                updateState {
                    it.copy(
                        languageSupportLevelMessage = LanguageSupportLevelMessage(
                            languageSupportLevel
                        )
                    )
                }
            }
        }
    }

    private suspend fun Action.LoadApp.listenForSessionUpdates() {
        sessionFlow
            .map { it.user }
            .distinctUntilChanged()
            .collectIn(viewModelScope) { user ->
                updateState { state ->
                    state.copy(
                        isLoggedIn = user.isLoggedIn
                    )
                }
            }
    }

    private suspend fun Action.LoadApp.listenForConfigUpdates() {
        viewModelScope.launch {
            appConfigFlow
                .distinctUntilChanged()
                .collectLatest { _ ->
                    updateState { state ->
                        state.copy(
                            isInMaintenanceMode = isInMaintenanceMode(),
                        )
                    }
                }
        }
    }

    private fun Action.LoadApp.listenForAppUpdateRequired() {
        viewModelScope.launch {
            isAppUpdateRequired()
                .distinctUntilChanged()
                .collectLatest { isUpdateRequired ->
                    updateState {
                        it.copy(isUpdateRequired = isUpdateRequired)
                    }
                }
        }
    }

    private val CoroutineScope.requiredStartupTasks: List<Deferred<Catching<Unit>>>
        get() = listOf(
            async { ensureSessionLoaded() },
            async { ensureAppConfigLoaded() },
        )

    sealed class Action {
        data object LoadApp : Action()
        data class LoadConsentStatus(val activity: Activity) : Action()
        data class StartInAppUpdate(
            val activity: Activity,
            val updateStatus: UpdateStatus.UpdateAvailable
        ) : Action()

        data object CompleteInAppUpdate : Action()

        data class MarkLanguageSupportLevelMessageShown(val languageSupportLevel: LanguageSupportLevel) :
            Action()
    }

    data class State(
        val isLoadingApp: Boolean,
        val isUpdateRequired: Boolean,
        val hasBlockingError: Boolean,
        val consentStatus: ConsentStatus?,
        val isInMaintenanceMode: Boolean,
        val inAppUpdateStatus: UpdateStatus?,
        val isLoggedIn: Boolean,
        val languageSupportLevelMessage: LanguageSupportLevelMessage?
    )

    data class LanguageSupportLevelMessage(
        val languageSupportLevel: LanguageSupportLevel
    )
}
