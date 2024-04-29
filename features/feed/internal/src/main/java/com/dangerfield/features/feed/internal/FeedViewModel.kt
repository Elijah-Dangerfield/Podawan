package com.dangerfield.features.feed.internal

import androidx.lifecycle.SavedStateHandle
import com.dangerfield.features.playback.PlaybackViewModel
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.getDisplayableEpisodes
import dagger.hilt.android.lifecycle.HiltViewModel
import podawan.core.doNothing
import podawan.core.eitherWay
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<FeedViewModel.State, FeedViewModel.Event, FeedViewModel.Action>(
    savedStateHandle,
    State()
) {

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
                episodes = it.episodes.map { ep ->
                    if (episode == ep) {
                        ep.copy(isPlaying = true)
                    } else {
                        ep.copy(isPlaying = false)
                    }
                }
            )
        }
    }

    private suspend fun Action.PauseEpisode.handlePause() {
        updateState {
            it.copy(
                episodes = it.episodes.map { ep ->
                    ep.copy(isPlaying = false)
                }
            )
        }
    }

    private suspend fun Action.Load.handleLoad() {
        updateState { it.copy(isLoading = true) }

        podcastRepository.getPodcast()
            .onSuccess { podcast ->
                val episodes = podcast.getDisplayableEpisodes()

                updateState {
                    it.copy(
                        podcastShow = podcast,
                        episodes = episodes,
                        isLoading = false
                    )
                }
            }.onFailure {
                sendEvent(Event.LoadFailed)
            }
            .eitherWay {
                updateState { it.copy(isLoading = false) }
            }
    }

    data class State(
        val isLoading: Boolean = false,
        val podcastShow: PodcastShow? = null,
        val episodes: List<DisplayableEpisode> = emptyList()
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
    }
}