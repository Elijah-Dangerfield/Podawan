package com.dangerfield.features.feed.internal

import androidx.lifecycle.SavedStateHandle
import com.dangerfield.features.feed.episodeIdArgument
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.getDisplayableEpisode
import dagger.hilt.android.lifecycle.HiltViewModel
import podawan.core.doNothing
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<EpisodeDetailsViewModel.State, EpisodeDetailsViewModel.Event, EpisodeDetailsViewModel.Action>(
    savedStateHandle,
    State()
) {

    val id = savedStateHandle.navArgument<String>(episodeIdArgument)

    init {
        takeAction(Action.Load)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Load -> action.handleLoad()
            is Action.PauseEpisode -> action.handlePause()
            is Action.PlayEpisode -> action.handlePlay()
            is Action.DownloadEpisode -> doNothing()
        }
    }

    private suspend fun Action.PlayEpisode.handlePlay() {
        updateState {
            it.copy(
                episode = it.episode?.copy(isPlaying = true),
            )
        }
    }

    private suspend fun Action.PauseEpisode.handlePause() {
        updateState {
            it.copy(
                episode = it.episode?.copy(isPlaying = false),
            )
        }
    }

    private suspend fun Action.Load.handleLoad() {
        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        podcastRepository.getPodcast()
            .onSuccess { podcast ->
                val episode = podcast.getDisplayableEpisode(id, removeHtml = false) ?: run {
                    sendEvent(Event.LoadFailed)
                    return@onSuccess
                }

                updateState { it.copy(episode = episode) }
            }.onFailure {
                sendEvent(Event.LoadFailed)
            }
    }

    data class State(
        val episode: DisplayableEpisode? = null,
    )

    sealed class Event {
        object LoadFailed : Event()
    }

    sealed class Action {
        object Load : Action()
        object PlayEpisode : Action()
        object PauseEpisode : Action()
        object DownloadEpisode : Action()
    }
}