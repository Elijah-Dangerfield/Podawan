package com.dangerfield.features.playback.internal

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import com.dangerfield.features.blockingerror.navigateToBlockingError
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.playback.episodeIdArgument
import com.dangerfield.features.playback.internal.ui.EpisodeOptionsBottomSheet
import com.dangerfield.features.playback.internal.ui.PlayerScreen
import com.dangerfield.features.playback.internal.ui.PlayerViewModel
import com.dangerfield.features.playback.internal.ui.PlayerViewModel.Action.Pause
import com.dangerfield.features.playback.internal.ui.PlayerViewModel.Action.Play
import com.dangerfield.features.playback.internal.ui.episodeOptions
import com.dangerfield.features.playback.internal.ui.openEpisodeOptions
import com.dangerfield.features.playback.playerRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.bottomSheet
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.GetDisplayableEpisode
import com.dangerfield.ui.components.FullScreenLoader
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import podawan.core.doNothing
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(
    private val getEpisode: GetDisplayableEpisode
) : GlobalNavBuilder {

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

            val currentlyPlaying = state.currentlyPlaying

            when {
                state.isLoading -> doNothing()
                currentlyPlaying == null -> router.navigateToBlockingError()
                else -> {
                    PlayerScreen(
                        episode = currentlyPlaying.episode,
                        onBackClicked = { router.dismissSheet(bottomSheetState) },
                        bottomSheetState = bottomSheetState,
                        onPause = { viewModel.takeAction(Pause) },
                        onPlay = { viewModel.takeAction(Play) },
                        onSeek = { viewModel.takeAction(PlayerViewModel.Action.Seek(it)) },
                        onRewind10 = { viewModel.takeAction(PlayerViewModel.Action.Rewind10Seconds) },
                        onSkip10 = { viewModel.takeAction(PlayerViewModel.Action.Skip10Seconds) },
                        onOptionsClicked = { router.openEpisodeOptions(currentlyPlaying.episode.id)}
                    )
                }
            }
        }

        bottomSheet(
            route = episodeOptions.navRoute,
            arguments = episodeOptions.navArguments
        ) {
            val id = it.navArgument<String>(episodeIdArgument) ?: return@bottomSheet
            var episode by remember { mutableStateOf<DisplayableEpisode?>(null) }
            val sheetState = rememberBottomSheetState()

            LaunchedEffect(Unit) {
                getEpisode(id).onSuccess { displayableEpisode ->
                    episode = displayableEpisode
                }
            }

            when(val ep = episode) {
                null -> FullScreenLoader()
                else -> EpisodeOptionsBottomSheet(
                    episode = ep,
                    bottomSheetState = sheetState,
                    onClose = { router.dismissSheet(sheetState) }
                )
            }
        }
    }
}