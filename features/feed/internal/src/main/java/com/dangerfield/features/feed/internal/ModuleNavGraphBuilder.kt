package com.dangerfield.features.feed.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToBlockingError
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.feed.episodeDetailsRoute
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.feed.toEpisodeDetails
import com.dangerfield.features.playback.PlaybackViewModel
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.ui.components.FullScreenLoader
import podawan.core.doNothing
import podawan.core.showDebugSnack
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : HomeTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = feedRoute.navRoute,
            arguments = feedRoute.navArguments
        ) {
            val viewModel: FeedViewModel = hiltViewModel()
            val playbackViewModel: PlaybackViewModel = hiltViewModel()

            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            ObserveWithLifecycle(flow = viewModel.eventFlow) {
                when (it) {
                    FeedViewModel.Event.LoadFailed -> router.navigateToGeneralErrorDialog()
                    FeedViewModel.Event.PlaybackFailed -> showDebugSnack { "Playback failed" }
                }
            }

            if (state.isLoading) {
                FullScreenLoader()
            } else {
                FeedScreen(
                    showTitle = state.podcastShow?.title.orEmpty(),
                    showDescription = state.podcastShow?.description.orEmpty(),
                    heroImageUrl = state.podcastShow?.image?.url,
                    episodes = state.episodes,
                    onEpisodePlayClicked = {
                        viewModel.takeAction(FeedViewModel.Action.PlayEpisode(it))
                        playbackViewModel.loadAndPlay(it.id)
                    },
                    onEpisodePauseClicked = {
                        viewModel.takeAction(FeedViewModel.Action.PauseEpisode(it))
                        playbackViewModel.takeAction(PlaybackViewModel.Action.Pause)
                    },
                    onEpisodeDownloadClicked = {
                        viewModel.takeAction(FeedViewModel.Action.DownloadEpisode(it))
                    },
                    onClickTitle = {
                        router.navigateToBlockingError()
                    },
                    onClickEpisode = {
                        router.toEpisodeDetails(it.id)
                    }
                )
            }
        }

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
                        onClickLink = { router.openWebLink(it) }
                    )
                }
            }
        }
    }
}