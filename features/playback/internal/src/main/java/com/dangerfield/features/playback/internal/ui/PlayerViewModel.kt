package com.dangerfield.features.playback.internal.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.navigation.episodeIdArgument
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.CurrentlyPlaying
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.GetCurrentlyPlaying
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getCurrentlyPlaying: GetCurrentlyPlaying,
    private val playerStateRepository: PlayerStateRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<PlayerViewModel.State, PlayerViewModel.Event, PlayerViewModel.Action>(
    savedStateHandle
) {

    private val id = savedStateHandle.navArgument<String>(episodeIdArgument)

    init {
        takeAction(Action.Load)
    }

    override fun initialState() = State(currentlyPlaying = null, isLoading = true)

    override suspend fun handleAction(action: Action) {
        with(action) {
            when (this) {
                is Action.Load -> handleLoad()
                is Action.Pause -> handlePause()
                is Action.Play -> handlePlay()
                is Action.Seek -> handleSeekTo(seconds.toInt())
                Action.Rewind10Seconds -> currentProgress()?.let { handleSeekTo(it - 10) }
                Action.Skip10Seconds -> currentProgress()?.let { handleSeekTo(it + 10) }
            }
        }
    }

    private suspend fun Action.Pause.handlePause() {
        updateState {
            it.copy(
                currentlyPlaying = it.currentlyPlaying?.copyWithPlayback {
                    EpisodePlayback.Paused(
                        duration = it.duration,
                        progress = it.progress
                    )
                },
            )
        }

        playerStateRepository.pauseEpisode()
    }


    private suspend fun Action.handleSeekTo(seconds: Int) {

        updateState {
            it.copy(
                currentlyPlaying = it.currentlyPlaying?.copyWithPlayback {
                    EpisodePlayback.Paused(
                        duration = it.duration,
                        progress = seconds.toDuration(SECONDS)
                    )
                },
            )
        }

        playerStateRepository.seekTo(seconds)
    }

    private suspend fun Action.Play.handlePlay() {
        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        updateState {
            it.copy(
                currentlyPlaying = it.currentlyPlaying?.copyWithPlayback {
                    EpisodePlayback.Playing(
                        duration = it.duration,
                        progress = it.progress
                    )
                },
            )
        }

        playerStateRepository.playEpisode(id)
    }

    private suspend fun Action.Load.handleLoad() {
        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        getCurrentlyPlaying().collectIn(viewModelScope) { currentlyPlaying ->
            if (currentlyPlaying == null) {
                sendEvent(Event.LoadFailed)
            } else {
                updateState {
                    it.copy(
                        currentlyPlaying = currentlyPlaying,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun currentProgress() =
        state.currentlyPlaying?.episode?.playback?.progress?.inWholeSeconds?.toInt()

    data class State(
        val currentlyPlaying: CurrentlyPlaying?,
        val isLoading: Boolean
    )

    sealed class Event {
        object LoadFailed : Event()
    }

    sealed class Action {
        object Load : Action()
        object Play : Action()
        object Pause : Action()
        object Skip10Seconds : Action()
        object Rewind10Seconds : Action()
        data class Seek(val seconds: Float) : Action()
    }
}