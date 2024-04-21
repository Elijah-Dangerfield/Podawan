package com.dangerfield.features.feed.internal

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
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
        Log.d("Elijah", "Initing feed view model. ${this.hashCode()}")
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
        Log.d("Elijah", "Loading Feed")
        updateState { it.copy(isLoading = true) }

        podcastRepository.getPodcast()
            .onSuccess { podcast ->
                Log.d("Elijah", "podcast loaded")

                val episodes = podcast.items.map { episode ->
                    DisplayableEpisode(
                        title = episode.title.orEmpty(),
                        releaseDate = episode.pubDate.orEmpty(),
                        imageUrl = episode.link.takeIf { it?.isNotBlank() == true } ?: podcast.image?.url.orEmpty(),
                        description = episode.description.orEmpty(),
                        fallbackImageUrl = podcast.image?.url.orEmpty(),
                        isPlaying = false,
                        isDownloaded = false,
                        id = episode.guid ?: episode.title.hashCode().toString()
                    )
                }
                updateState {
                    it.copy(
                        podcastShow = podcast,
                        episodes = episodes,
                        isLoading = false
                    )
                }
            }.onFailure {
                Log.d("Elijah", "podcast failed to load")

                sendEvent(Event.LoadFailed)
            }
            .eitherWay {
                Log.d("Elijah", "Hit either, setting loading to false")
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
    }

    sealed class Action {
        object Load : Action()
        class PlayEpisode(val episode: DisplayableEpisode) : Action()
        class PauseEpisode(val episode: DisplayableEpisode) : Action()
        class DownloadEpisode(val episode: DisplayableEpisode) : Action()
    }
}