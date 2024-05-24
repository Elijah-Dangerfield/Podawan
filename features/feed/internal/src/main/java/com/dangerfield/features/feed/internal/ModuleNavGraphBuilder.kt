package com.dangerfield.features.feed.internal

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.feed.episodeDetailsRoute
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.feed.showDetailsRoute
import com.dangerfield.features.feed.toEpisodeDetails
import com.dangerfield.features.feed.toShowDetails
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.screen
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
                    showTitle = state.showTitle,
                    showDescription = state.showDescription,
                    heroImageUrl = state.showHeroImageUrl,
                    episodes = state.episodes,
                    currentlyPlayingEpisode = state.currentlyPlaying?.episode,
                    onEpisodePlayClicked = {
                        viewModel.takeAction(FeedViewModel.Action.PlayEpisode(it))
                    },
                    onEpisodePauseClicked = {
                        viewModel.takeAction(FeedViewModel.Action.PauseEpisode(it))
                    },
                    onEpisodeDownloadClicked = {
                        viewModel.takeAction(FeedViewModel.Action.DownloadEpisode(it))
                    },
                    onHeaderClicked = {
                        router.toShowDetails()
                    },
                    onClickEpisode = {
                        router.toEpisodeDetails(it.id)
                    },
                    onCurrentlyPlayingExitView = {
                        viewModel.takeAction(FeedViewModel.Action.CurrentlyPlayingNotShowing)
                    },
                    onCurrentlyPlayingEnterView = {
                        viewModel.takeAction(FeedViewModel.Action.CurrentlyPlayingShowing)
                    },
                    onAddToPlaylistClicked = { doNothing() }
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
                        onAddToPlaylistClicked = { doNothing() }
                    )
                }
            }
        }

        screen(route = showDetailsRoute) {
            val viewModel: ShowDetailsViewModel = hiltViewModel()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            when (val show = state.show) {
                null -> FullScreenLoader()
                else -> ShowDetailsScreen(
                    onNavigateBack = { router.goBack() },
                    showTitle = show.title.orEmpty(),
                    showDescription = show.description.orEmpty(),
                    heroImageUrl = show.heroImage?.url,
                    episodeCount = show.episodes.size,
                    categories = show.itunesShowData?.categories.orEmpty()
                )
            }
        }
    }
}