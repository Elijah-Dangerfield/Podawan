package com.dangerfield.features.episodeDetails

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.playlist.navigateToAddToPlaylist
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.ui.components.FullScreenLoader
import podawan.core.doNothing

fun NavGraphBuilder.registerEpisodeDetails(
    router: Router,
    route: Route.Template,
) {
    composable(
        route = route.navRoute,
        arguments = route.navArguments
    ) {
        val viewModel: EpisodeDetailsViewModel = hiltViewModel()

        val state by viewModel.stateFlow.collectAsStateWithLifecycle()

        ObserveWithLifecycle(flow = viewModel.eventFlow) {
            when (it) {
                EpisodeDetailsViewModel.Event.LoadFailed -> router.navigateToGeneralErrorDialog()
            }
        }

        state.episode.let { episode ->
            if (episode == null) {
                FullScreenLoader()
            } else {
                EpisodeDetailsScreen(
                    episode = episode,
                    isCurrentlyPlaying = state.isCurrentlyPlaying,
                    onPauseClicked = {
                        viewModel.takeAction(EpisodeDetailsViewModel.Action.PauseEpisode)
                    },
                    onPlayClicked = {
                        viewModel.takeAction(EpisodeDetailsViewModel.Action.PlayEpisode)
                    },
                    onDownloadClicked = {
                        viewModel.takeAction(EpisodeDetailsViewModel.Action.DownloadEpisode)
                    },
                    onShareClicked = { doNothing() },
                    onNavigateBack = { router.goBack() },
                    onClickLink = { router.openWebLink(it) },
                    onAddToPlaylistClicked = {
                        router.navigateToAddToPlaylist(episode.id)
                    }
                )
            }
        }
    }
}