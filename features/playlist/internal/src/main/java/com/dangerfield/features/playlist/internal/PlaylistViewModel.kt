package com.dangerfield.features.playlist.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.features.playlist.MapToPlaylistWithEpisodes
import com.dangerfield.features.playlist.PlaylistRepository
import com.dangerfield.features.playlist.playlistIdArgument
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.coreflowroutines.collectWithPrevious
import com.dangerfield.libraries.navigation.navArgument
import com.dangerfield.libraries.podcast.CurrentlyPlaying
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.EpisodePlayback.None
import com.dangerfield.libraries.podcast.EpisodePlayback.Paused
import com.dangerfield.libraries.podcast.GetCurrentlyPlaying
import com.dangerfield.libraries.podcast.HeroImage
import com.dangerfield.libraries.podcast.MapEpisodesToDisplayable
import com.dangerfield.libraries.podcast.duration
import com.dangerfield.libraries.podcast.isLoading
import com.dangerfield.libraries.podcast.isPlaying
import com.dangerfield.libraries.podcast.progress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import podawan.core.allOrNone
import podawan.core.doNothing
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playerStateRepository: PlayerStateRepository,
    private val mapEpisodesToDisplayable: MapEpisodesToDisplayable,
    private val mapToPlaylistWithEpisodes: MapToPlaylistWithEpisodes,
    private val getCurrentlyPlaying: GetCurrentlyPlaying,
    private val playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<PlaylistViewModel.State, PlaylistViewModel.Event, PlaylistViewModel.Action>(
    savedStateHandle,
    State()
) {

    private var shouldObserveCurrentlyPlayingUpdates: Boolean = false
    private val id = savedStateHandle.navArgument<Int>(playlistIdArgument)

    init {
        takeAction(Action.Load)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Load -> action.handleLoad()
            is Action.PauseEpisode -> action.handlePause()
            is Action.PlayEpisode -> action.handlePlay()
            is Action.DownloadEpisode -> doNothing()
            is Action.CurrentlyPlayingShowing -> action.handleCurrentlyPlayingShowing()
            is Action.CurrentlyPlayingNotShowing -> action.handleCurrentlyPlayingNotShowing()
        }
    }

    private suspend fun Action.PlayEpisode.handlePlay() {
        takeAction(Action.CurrentlyPlayingShowing)
        updateState { state ->
            state.copy(
                episodes = state.episodes.withUpdatedEpisode(
                    predicate = { it.isPlaying || it.isLoading },
                    update = {
                        it.copy(
                            playback = None(
                                progress = it.progress,
                                duration = it.duration
                            )
                        )
                    }
                ).withUpdatedEpisode(
                    predicate = { it.id == episode.id },
                    update = {
                        it.copy(
                            playback = EpisodePlayback.Playing(
                                progress = it.progress,
                                duration = it.duration
                            )
                        )
                    }
                )
            )
        }

        playerStateRepository.playEpisode(episode.id)
    }

    private suspend fun Action.PauseEpisode.handlePause() {
        updateState { state ->
            state.copy(
                episodes = state.episodes.withUpdatedEpisode(
                    predicate = { it.id == episode.id },
                    update = {
                        it.copy(
                            playback = Paused(
                                progress = it.progress,
                                duration = it.duration
                            )
                        )
                    }
                )
            )
        }
        playerStateRepository.pauseEpisode()
    }

    private fun List<DisplayableEpisode>.withUpdatedEpisode(
        predicate: (DisplayableEpisode) -> Boolean,
        update: (DisplayableEpisode) -> DisplayableEpisode
    ): ImmutableList<DisplayableEpisode> {
        val updatedList = this.toMutableList()
        updatedList.forEachIndexed { index, ep ->
            if (predicate(ep)) {
                updatedList[index] = update(ep)
                return@forEachIndexed
            }
        }

        return updatedList.toPersistentList()
    }

    private suspend fun Action.Load.handleLoad() {

        if (id == null) {
            sendEvent(Event.LoadFailed)
            return
        }

        updateState { it.copy(isLoading = true) }

        playlistRepository.getPlaylistFlow(id)
            .onSuccess { flow ->
                flow.collectIn(viewModelScope) { playlist ->
                    val playlistWithEpisodes = mapToPlaylistWithEpisodes(playlist)
                    val episodes = mapEpisodesToDisplayable(playlistWithEpisodes.episodes).getOrNull()

                    if (episodes == null) {
                        sendEvent(Event.LoadFailed)
                    } else {
                        updateState {
                            it.copy(
                                isLoading = false,
                                name = playlist.name,
                                description = playlist.description,
                                episodes = episodes,
                                heroImageUrl = playlist.imageUrl
                            )
                        }
                    }
                }
            }
            .onFailure { sendEvent(Event.LoadFailed) }

        viewModelScope.launch {
            getCurrentlyPlaying().collectWithPrevious { prev, cur ->
                val didEpisodeChange =
                    allOrNone(prev?.episode?.id, cur?.episode?.id) { prevId, curId ->
                        curId != prevId
                    } ?: false

                when {
                    shouldObserveCurrentlyPlayingUpdates -> {
                        updateState { it.copy(currentlyPlaying = cur) }
                    }

                    didEpisodeChange -> {
                        updateState { it.copy(currentlyPlaying = cur) }
                    }

                    else -> doNothing()
                }
            }
        }
    }

    private suspend fun Action.CurrentlyPlayingShowing.handleCurrentlyPlayingShowing() {
        shouldObserveCurrentlyPlayingUpdates = true
    }

    private suspend fun Action.CurrentlyPlayingNotShowing.handleCurrentlyPlayingNotShowing() {
        shouldObserveCurrentlyPlayingUpdates = true
    }

    data class State(
        val isLoading: Boolean = false,
        val name: String = "",
        val description: String = "",
        val heroImageUrl: String? = null,
        /**
         * Null if no episode is playing
         * this field updates
         * 1. if being displayed = on every change/update
         * 2. if not being displayed = on every change of episode id
         */
        val currentlyPlaying: CurrentlyPlaying? = null,
        val episodes: ImmutableList<DisplayableEpisode> = persistentListOf(),
    )

    sealed class Event {
        object LoadFailed : Event()
        object PlaybackFailed : Event()
    }

    sealed class Action {
        object Load : Action()
        class PlayEpisode(val episode: DisplayableEpisode) : Action()
        class PauseEpisode(val episode: DisplayableEpisode) : Action()

        class DownloadEpisode(val episode: DisplayableEpisode) : Action()

        object CurrentlyPlayingShowing : Action()

        object CurrentlyPlayingNotShowing : Action()
    }
}