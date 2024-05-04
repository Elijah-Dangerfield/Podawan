package com.dangerfield.libraries.app

import com.dangerfield.libraries.navigation.NavGraphRegistry
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dangerfield.features.auth.loginRoute
import com.dangerfield.features.blockingerror.blockingErrorRoute
import com.dangerfield.features.blockingerror.maintenanceRoute
import com.dangerfield.features.consent.consentRoute
import com.dangerfield.features.forcedupdate.forcedUpdateNavigationRoute
import com.dangerfield.features.inAppMessaging.UpdateStatus
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.features.playback.PlayerState
import com.dangerfield.features.playback.internal.MediaPlayerService
import com.dangerfield.features.playback.openPlayer
import com.dangerfield.libraries.analytics.LocalMetricsTracker
import com.dangerfield.libraries.analytics.MetricsTracker
import com.dangerfield.libraries.app.AppViewModel.Action.LoadConsentStatus
import com.dangerfield.libraries.app.AppViewModel.Action.MarkLanguageSupportLevelMessageShown
import com.dangerfield.libraries.app.startup.SplashScreenBuilder
import com.dangerfield.libraries.dictionary.Dictionary
import com.dangerfield.libraries.dictionary.LocalDictionary
import com.dangerfield.libraries.dictionary.internal.ui.navigateToLanguageSupportDialog
import com.dangerfield.libraries.navigation.DelegatingRouter
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.mainGraphRoute
import com.dangerfield.libraries.network.NetworkMonitor
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.LocalAppState
import com.dangerfield.libraries.ui.LocalBuildInfo
import com.dangerfield.libraries.ui.LocalColors
import com.dangerfield.libraries.ui.color.Colors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import podawan.core.AppConfiguration
import podawan.core.BuildInfo
import podawan.core.Message
import podawan.core.SnackBarPresenter
import podawan.core.doNothing
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

open class AppActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels()

    @Inject
    lateinit var playerStateRepository: PlayerStateRepository

    @Inject
    lateinit var splashScreenBuilder: SplashScreenBuilder

    @Inject
    lateinit var metricsTracker: MetricsTracker

    @Inject
    lateinit var buildInfo: BuildInfo

    @Inject
    lateinit var dictionary: Dictionary

    @Inject
    lateinit var colors: Colors

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var appConfiguration: AppConfiguration

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var delegatingRouter: DelegatingRouter

    @Inject
    lateinit var navGraphRegistry: NavGraphRegistry

    private var hasDrawnApp = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreenBuilder
            .keepOnScreenWhile { !hasDrawnApp.get() }
            .build(this)


        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    appViewModel.stateFlow.first { !it.isLoadingApp }
                    if (!hasDrawnApp.getAndSet(true)) {
                        setAppContent()
                        appViewModel.takeAction(LoadConsentStatus(this@AppActivity))
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {
                    playerStateRepository.getPlayerStateFlow().first { it is PlayerState.ReadyToPlay }
                    MediaPlayerService.start(applicationContext)
                }
            }
        }
    }

    private fun setAppContent() {
        setContent {

            val state by appViewModel.stateFlow.collectAsStateWithLifecycle()

            val startingRouteTemplate by remember {
                derivedStateOf {
                    when {
                        state.isUpdateRequired -> forcedUpdateNavigationRoute
                        state.isInMaintenanceMode -> maintenanceRoute
                        state.hasBlockingError -> blockingErrorRoute
                        state.isConsentNeeded -> consentRoute
                        state.isLoggedIn -> mainGraphRoute
                        else -> loginRoute
                    }
                }
            }

            val appState = rememberAppState(networkMonitor = networkMonitor)

            val isAppUpdateStatus by remember {
                derivedStateOf { state.inAppUpdateStatus }
            }

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

            CompositionLocalProvider(
                LocalColors provides colors,
                LocalAppConfiguration provides appConfiguration,
                LocalMetricsTracker provides metricsTracker,
                LocalDictionary provides dictionary,
                LocalBuildInfo provides buildInfo,
                LocalAppState provides appState,
            ) {
                PodawanApp(
                    startingRouteTemplate = startingRouteTemplate,
                    delegatingRouter = delegatingRouter,
                    navGraphRegistry = navGraphRegistry,
                    updateStatus = isAppUpdateStatus,
                    currentlyPlayingEpisode = state.currentlyPlayingEpisode,
                    onPauseEpisode = { appViewModel.takeAction(AppViewModel.Action.PauseEpisode(it)) },
                    onPlayEpisode = { appViewModel.takeAction(AppViewModel.Action.PlayEpisode(it)) },
                    onClickBottomPlayerBar = { router.openPlayer(it.episode.id) }
                )
            }
        }
    }

    private fun handleLanguageSupportMessage(message: AppViewModel.LanguageSupportLevelMessage?) {
        if (message != null) {

            appViewModel.takeAction(
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
                    appViewModel.startInAppUpdate(this@AppActivity, updateStatus)
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
                            action = appViewModel::installUpdate
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
