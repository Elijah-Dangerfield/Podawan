package com.dangerfield.features.feed.internal

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.blockingerror.navigateToBlockingError
import com.dangerfield.features.blockingerror.navigateToGeneralErrorDialog
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.libraries.coreflowroutines.ObserveWithLifecycle
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import se.ansman.dagger.auto.AutoBindIntoSet
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.ui.components.CircularProgressIndicator
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor() : HomeTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        Log.d("Elijah", "Adding feed destination")
        composable(
            route = feedRoute.navRoute,
            arguments = feedRoute.navArguments
        ) {

            val viewModel: FeedViewModel = hiltViewModel()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            ObserveWithLifecycle(flow = viewModel.eventFlow) {
                when (it) {
                    FeedViewModel.Event.LoadFailed -> router.navigateToGeneralErrorDialog()
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                    CircularProgressIndicator()
                }
            } else {
                FeedScreen(
                    title = state.podcastShow?.title.orEmpty(),
                    episodes = state.episodes,
                    onEpisodePlayClicked = {
                        viewModel.takeAction(FeedViewModel.Action.PlayEpisode(it))
                    },
                    onEpisodePauseClicked = {
                        viewModel.takeAction(FeedViewModel.Action.PauseEpisode(it))
                    },
                    onEpisodeDownloadClicked = {
                        viewModel.takeAction(FeedViewModel.Action.DownloadEpisode(it))
                    },
                    onClickTitle =  {
                        router.navigateToBlockingError()
                    }
                )
            }
        }
    }
}