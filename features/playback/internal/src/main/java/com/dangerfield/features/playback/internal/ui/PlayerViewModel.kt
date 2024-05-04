package com.dangerfield.features.playback.internal.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.features.playback.episodeIdArgument
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.CurrentlyPlayingEpisode
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.GetCurrentlyPlayingEpisode
import com.dangerfield.libraries.podcast.GetDisplayableEpisode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import podawan.core.eitherWay
import podawan.core.logOnFailure
import kotlin.time.toDuration
import javax.inject.Inject
import kotlin.time.DurationUnit.SECONDS

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getCurrentlyPlayingEpisode: GetCurrentlyPlayingEpisode,
    private val getDisplayableEpisode: GetDisplayableEpisode,
    private val playerStateRepository: PlayerStateRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<PlayerViewModel.State, PlayerViewModel.Event, PlayerViewModel.Action>(
    savedStateHandle
) {

    private val id = savedStateHandle.navArgument<String>(episodeIdArgument)

    init {
        takeAction(Action.Load)
    }

    override fun initialState() = State(episode = null, isLoading = true)

    override suspend fun handleAction(action: Action) {
        with(action) {
            when (this) {
                is Action.Load -> handleLoad()
                is Action.Pause -> handlePause()
                is Action.Play -> handlePlay()
                is Action.Seek -> handleSeek()
            }
        }
    }

    private suspend fun Action.Pause.handlePause() {
        updateState {
            it.copy(
                episode = it.episode?.let { currentEpisode ->
                    currentEpisode.copy(
                        episodePlayback = EpisodePlayback.Paused(
                            duration = currentEpisode.episodePlayback.duration,
                            progress = currentEpisode.episodePlayback.progress
                        )
                    )
                }
            )
        }

        playerStateRepository.pauseEpisode()
    }

    private suspend fun Action.Seek.handleSeek() {
        updateState {
            it.copy(
                episode = it.episode?.let { currentEpisode ->
                    currentEpisode.copy(
                        episodePlayback = EpisodePlayback.Paused(
                            duration = currentEpisode.episodePlayback.duration,
                            progress = seconds.toInt().toDuration(SECONDS)
                        )
                    )
                }
            )
        }

        playerStateRepository.seekTo(seconds.toInt())
    }

    private suspend fun Action.Play.handlePlay() {
        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        updateState {
            it.copy(
                episode = it.episode?.let { currentEpisode ->
                    currentEpisode.copy(
                        episodePlayback = EpisodePlayback.Playing(
                            duration = currentEpisode.episodePlayback.duration,
                            progress = currentEpisode.episodePlayback.progress
                        )
                    )
                }
            )

        }

        playerStateRepository.playEpisode(id)
    }

    private suspend fun Action.Load.handleLoad() {
        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        combine(
            getDisplayableEpisode(id).map { it.logOnFailure().getOrNull() },
            getCurrentlyPlayingEpisode()
        ) { displayableEpisode, currentlyPlayingEpisode ->
            when {
                currentlyPlayingEpisode == null && displayableEpisode == null -> { sendEvent(Event.LoadFailed) }
                displayableEpisode != null && currentlyPlayingEpisode == null -> {
                    State(
                        episode = CurrentlyPlayingEpisode(
                            episode = displayableEpisode,
                            episodePlayback = EpisodePlayback.None()
                        ),
                        isLoading = false
                    )
                }
                else -> {
                    State(episode = currentlyPlayingEpisode, isLoading = false)
                }
            }

        }.launchIn(viewModelScope)
    }


    data class State(
        val episode: CurrentlyPlayingEpisode?,
        val isLoading: Boolean
    )

    sealed class Event {
        object LoadFailed : Event()
    }

    sealed class Action {
        object Load : Action()
        object Play : Action()
        object Pause : Action()
        data class Seek(val seconds: Float) : Action()
    }
}