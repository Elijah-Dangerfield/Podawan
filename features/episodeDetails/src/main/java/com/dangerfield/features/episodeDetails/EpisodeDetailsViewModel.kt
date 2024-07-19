package com.dangerfield.features.episodeDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.GetCurrentlyPlaying
import com.dangerfield.libraries.podcast.GetDisplayableEpisode
import dagger.hilt.android.lifecycle.HiltViewModel
import podawan.core.doNothing
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val getDisplayableEpisode: GetDisplayableEpisode,
    private val getCurrentlyPlaying: GetCurrentlyPlaying,
    private val playerStateRepository: PlayerStateRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<EpisodeDetailsViewModel.State, EpisodeDetailsViewModel.Event, EpisodeDetailsViewModel.Action>(
    savedStateHandle,
    State()
) {

    private val id = savedStateHandle.navArgument<String>(episodeIdArgument)

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
        val episodeId = id ?: return
        updateState {
            it.copy(
                episode = it.episode?.copy(
                    playback = EpisodePlayback.Playing(
                        progress = it.episode.playback.progress,
                        duration = it.episode.playback.duration
                    )
                )
            )
        }

        playerStateRepository.playEpisode(episodeId)
    }

    private suspend fun Action.PauseEpisode.handlePause() {
        updateState {
            it.copy(
                episode = it.episode?.copy(
                    playback = EpisodePlayback.Paused(
                        progress = it.episode.playback.progress, 
                        duration = it.episode.playback.duration
                    )
                )
            )
        }

        playerStateRepository.pauseEpisode()
    }

    private suspend fun Action.Load.handleLoad() {
        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        getDisplayableEpisode(id)
            .onSuccess { episode -> updateState { it.copy(episode = episode) } }
            .onFailure { sendEvent(Event.LoadFailed) }

        getCurrentlyPlaying().collectIn(viewModelScope) { currentlyPlaying ->
            if (currentlyPlaying?.episode?.id == id) {
                updateState { currentState ->
                    currentState.copy(
                        episode = currentlyPlaying.episode,
                        isCurrentlyPlaying = true
                    )
                }
            }
        }
    }

    data class State(
        val episode: DisplayableEpisode? = null,
        val isCurrentlyPlaying: Boolean = false
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