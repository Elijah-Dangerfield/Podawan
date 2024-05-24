package com.dangerfield.features.feed.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.feed.episodeIdArgument
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.GetCurrentlyPlaying
import com.dangerfield.libraries.podcast.GetDisplayableEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import podawan.core.doNothing
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShowDetailsViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<ShowDetailsViewModel.State, ShowDetailsViewModel.Event, ShowDetailsViewModel.Action>(
    savedStateHandle,
    State()
) {

    init {
        takeAction(Action.Load)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Load -> action.handleLoad()
        }
    }



    private suspend fun Action.Load.handleLoad() {
        podcastRepository.getPodcast().onSuccess { show ->
            updateState {
                it.copy(show = show)
            }
        }
    }

    data class State(
        val show: PodcastShow? = null,
    )

    sealed class Event {
        object LoadFailed : Event()
    }

    sealed class Action {
        object Load : Action()
    }
}