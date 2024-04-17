package com.dangerfield.libraries.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.dangerfield.features.inAppMessaging.UpdateStatus
import com.dangerfield.libraries.coreflowroutines.waitFor
import com.dangerfield.libraries.dictionary.internal.ui.navigateToLanguageSupportDialog
import com.dangerfield.libraries.navigation.BuildNavHost
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.app.AppViewModel.Action.LoadConsentStatus
import com.dangerfield.libraries.app.AppViewModel.Action.MarkLanguageSupportLevelMessageShown
import com.dangerfield.libraries.app.startup.SplashScreenBuilder
import com.dangerfield.libraries.ui.color.ColorResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import oddoneout.core.AppConfiguration
import podawan.core.Message
import podawan.core.SnackBarPresenter
import podawan.core.doNothing
import javax.inject.Inject

open class AppActivity : ComponentActivity() {

    private val mainActivityViewModel: AppViewModel by viewModels()

    @Inject
    lateinit var splashScreenBuilder: SplashScreenBuilder

    @Inject
    lateinit var buildNavHostMultiGraph: BuildNavHost

    @Inject
    lateinit var compositionLocalsProvider: CompositionLocalsProvider

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var isLoading: Boolean by mutableStateOf(true)

        splashScreenBuilder
            .keepOnScreenWhile { isLoading }
            .build(this)

        lifecycleScope.launch {
            // we do not set content loaded, this allows the splash screen to animate
            mainActivityViewModel.stateFlow.waitFor { !it.isLoadingApp }
            isLoading = false
            setAppContent()
            loadConsentStatus()
        }
    }

    private fun loadConsentStatus() {
        mainActivityViewModel.takeAction(LoadConsentStatus(this))
    }

    private fun setAppContent() {
        setContent {
            val state by mainActivityViewModel.stateFlow.collectAsStateWithLifecycle()

            LaunchedEffect(state.languageSupportLevelMessage) {
                state.languageSupportLevelMessage?.let {
                    handleLanguageSupportMessage(it)
                }
            }

            LaunchedEffect(state.inAppUpdateStatus) {
                state.inAppUpdateStatus?.let {
                    handleInAppUpdateStatus(it)
                }
            }

            compositionLocalsProvider {
                PodawanApp(
                    isUpdateRequired = state.isUpdateRequired,
                    hasBlockingError = state.hasBlockingError,
                    consentStatus = state.consentStatus,
                    isInMaintenanceMode = state.isInMaintenanceMode,
                    updateStatus = state.inAppUpdateStatus,
                    buildNavHost = {
                        buildNavHostMultiGraph(it)
                    },
                    accentColor = ColorResource.MintyFresh300,
                    isLoggedIn = state.isLoggedIn,
                )
            }
        }
    }

    private fun handleLanguageSupportMessage(message: AppViewModel.LanguageSupportLevelMessage?) {
        if (message != null) {

            mainActivityViewModel.takeAction(
                MarkLanguageSupportLevelMessageShown(
                    message.languageSupportLevel
                )
            )

            router.navigateToLanguageSupportDialog(
                supportLevelName = message.languageSupportLevel.name,
                languageDisplayName = message.languageSupportLevel.locale.displayLanguage
            )
        }
    }

    private fun handleInAppUpdateStatus(updateStatus: UpdateStatus) {
        when (updateStatus) {
            is UpdateStatus.UpdateAvailable -> {
                if (updateStatus.shouldUpdate) {
                    mainActivityViewModel.startInAppUpdate(this@AppActivity, updateStatus)
                }
            }

            is UpdateStatus.Downloaded -> {
                if (updateStatus.wasBackgroundUpdate) {
                    SnackBarPresenter.showMessage(
                        message = Message(
                            title = "Your update is ready",
                            message = "Your update has finished downloading. Tap install to use the newest version.",
                            autoDismiss = false,
                            actionLabel = "Install",
                            action = mainActivityViewModel::installUpdate
                        )
                    )
                }
            }

            is UpdateStatus.Failed,
            UpdateStatus.InvalidUpdateRequest -> {
                SnackBarPresenter.showMessage(
                    message = Message(
                        message = "We encountered a problem upgrading. Please try again in the app store.",
                        autoDismiss = true
                    )
                )
            }

            else -> doNothing()
        }
    }
}
