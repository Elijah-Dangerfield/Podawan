package com.dangerfield.features.feed.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.feed.episodeDetailsRoute
import com.dangerfield.features.playlist.navigateToAddToPlaylist
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.ui.components.FullScreenLoader
import podawan.core.doNothing
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class GlobalNavGraphBuilder @Inject constructor() : GlobalNavBuilder {
    override fun NavGraphBuilder.addDestinations(router: Router) {

        composable(
            route = episodeDetailsRoute.navRoute,
            arguments = episodeDetailsRoute.navArguments
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
}