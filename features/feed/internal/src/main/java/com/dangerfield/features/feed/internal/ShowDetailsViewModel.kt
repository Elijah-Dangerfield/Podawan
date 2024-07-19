package com.dangerfield.features.feed.internal

import androidx.lifecycle.SavedStateHandle
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import dagger.hilt.android.lifecycle.HiltViewModel
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