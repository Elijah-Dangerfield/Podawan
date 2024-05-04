package com.dangerfield.features.feed.internal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.EpisodePlayback.None
import com.dangerfield.libraries.podcast.EpisodePlayback.Paused
import com.dangerfield.libraries.podcast.GetDisplayableEpisodes
import com.dangerfield.libraries.podcast.PodcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import podawan.core.doNothing
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val podcastRepository: PodcastRepository,
    private val playerStateRepository: PlayerStateRepository,
    private val getDisplayableEpisodes: GetDisplayableEpisodes,
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
        updateState { state ->
            state.copy(
                episodes = state.episodes.withUpdatedEpisode(
                    predicate = { it.isPlaying },
                    update = { it.copy(isPlaying = false) }
                ).withUpdatedEpisode(
                    predicate = { it.id == episode.id },
                    update = { it.copy(isPlaying = true) }
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
                    update = { it.copy(isPlaying = false) }
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
        updateState { it.copy(isLoading = true) }

        podcastRepository.getPodcast().onSuccess { show ->
            getDisplayableEpisodes(show).collectIn(viewModelScope) {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        episodes = it,
                        showTitle = show.title.orEmpty(),
                        showDescription = show.description.orEmpty(),
                        showHeroImageUrl = show.itunesShowData?.image
                            ?: show.heroImage?.url.orEmpty()
                    )
                }
            }
        }
            .onFailure { sendEvent(Event.LoadFailed) }
    }

    // TODO its probably much more efficient to just keep a currentPlaying episode in the state
    /*
    so right now this list is updated every single second with just 1 item changing
    that change might not even get used by the ui.

    I should change the way we represent a playing episode in the state to just be a single item
     */
    data class State(
        val isLoading: Boolean = false,
        val showTitle: String = "",
        val showDescription: String = "",
        val showHeroImageUrl: String = "",
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
    }
}