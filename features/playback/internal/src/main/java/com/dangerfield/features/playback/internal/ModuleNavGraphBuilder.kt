package com.dangerfield.features.playback.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToBlockingError
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.playback.internal.ui.PlayerScreen
import com.dangerfield.features.playback.internal.ui.PlayerViewModel
import com.dangerfield.features.playback.internal.ui.PlayerViewModel.Action.Pause
import com.dangerfield.features.playback.internal.ui.PlayerViewModel.Action.Play
import com.dangerfield.features.playback.playerRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.bottomSheet
import com.dangerfield.ui.components.FullScreenLoader
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : GlobalNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        bottomSheet(
            route = playerRoute.navRoute,
            arguments = playerRoute.navArguments
        ) {
            val viewModel: PlayerViewModel = hiltViewModel()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()
            val bottomSheetState = rememberBottomSheetState()

            ObserveWithLifecycle(flow = viewModel.eventFlow) {
                when (it) {
                    PlayerViewModel.Event.LoadFailed -> router.navigateToGeneralErrorDialog()
                }
            }

            val episode = state.episode
            when {
                state.isLoading -> FullScreenLoader()
                episode == null -> router.navigateToBlockingError()
                else -> {
                    PlayerScreen(
                        episode = episode,
                        onBackClicked = { router.dismissSheet(bottomSheetState) },
                        bottomSheetState = bottomSheetState,
                        onPause = { viewModel.takeAction(Pause) },
                        onPlay = { viewModel.takeAction(Play) },
                        onSeek = { viewModel.takeAction(PlayerViewModel.Action.Seek(it)) }
                    )
                }
            }
        }
    }
}