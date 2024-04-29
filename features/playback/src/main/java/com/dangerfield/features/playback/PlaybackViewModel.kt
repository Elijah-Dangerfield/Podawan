package com.dangerfield.features.playback

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.dangerfield.features.playback.PlaybackState.Buffering
import com.dangerfield.features.playback.PlaybackState.Playing
import com.dangerfield.libraries.coreflowroutines.SEAViewModel
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.PodcastEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import podawan.core.ifLinkFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackHandler: PlaybackHandler,
    private val podcastRepository: PodcastRepository,
    savedStateHandle: SavedStateHandle
) : SEAViewModel<PlaybackViewModel.State, PlaybackViewModel.Event, PlaybackViewModel.Action>(
    savedStateHandle = savedStateHandle,
    initialStateArg = State()
) {

    init {
        takeAction(Action.Load)
    }

    override suspend fun handleAction(action: Action) {
        when (action) {
            is Action.Load -> action.loadPlayer()
            is Action.Play -> playbackHandler.onPlayerEvent(PlayerEvent.Play)
            is Action.Pause -> playbackHandler.onPlayerEvent(PlayerEvent.Pause)
            is Action.Stop -> playbackHandler.onPlayerEvent(PlayerEvent.Stop)
            is Action.Previous -> playbackHandler.onPlayerEvent(PlayerEvent.Previous)
            is Action.Next -> playbackHandler.onPlayerEvent(PlayerEvent.Next)
            is Action.UpdateProgress -> {
                action.updateState {
                    it.copy(progress = action.newProgress.toLong())
                }
                playbackHandler.onPlayerEvent(
                    PlayerEvent.UpdateProgress(action.newProgress)
                )
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            playbackHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }

    private fun formatDuration(duration: Long): String {
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun loadAndPlay(episodeId: String) {
        viewModelScope.launch {
            podcastRepository.getEpisode(episodeId)
                .onSuccess {
                    setEpisode(it)
                    takeAction(Action.Play)
                }
                .onFailure {
                    sendEvent(Event.PlaybackFailed)
                }
        }
    }

    fun setEpisode(episode: PodcastEpisode) {
        val imageToUse =
            episode.itunesItemData?.image?.ifLinkFormat()
            ?: episode.image?.ifLinkFormat()
            ?: episode.showHeroImage?.url?.ifLinkFormat()

        val mediaItem = MediaItem.Builder()
            .setUri(episode.audio)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setArtworkUri(Uri.parse(imageToUse))
                    .setAlbumTitle(episode.title)
                    .setDisplayTitle(episode.title)
                    .build()
            ).build()

        playbackHandler.addMediaItem(mediaItem)
    }

    private fun Action.Load.loadPlayer() {
        viewModelScope.launch {
            playbackHandler.playbackState.collect { handlerPlaybackState ->
                updateState {

                    val isError = handlerPlaybackState is PlaybackState.Error

                    if (isError) {
                        sendEvent(Event.PlaybackFailed)
                        return@updateState it
                    }
                    val isPlaying = handlerPlaybackState is Playing && handlerPlaybackState.isPlaying

                    val currentProgress = when (handlerPlaybackState) {
                        is Buffering -> handlerPlaybackState.progress
                        is PlaybackState.Progress -> handlerPlaybackState.progress
                        else -> it.progress
                    }

                    val duration = when (handlerPlaybackState) {
                        is PlaybackState.Ready -> handlerPlaybackState.duration
                        else -> it.duration
                    }

                    val progress = if (currentProgress > 0) (currentProgress.toFloat() / duration) else 0f

                    val progressString = formatDuration(currentProgress)

                    it.copy(
                        progress = currentProgress,
                        playbackState = handlerPlaybackState,
                        isPlaying = isPlaying,
                        progressPercent = progress,
                        displayProgress = progressString,
                    )
                }
            }
        }
    }

    @Parcelize // to make state savable by SEAViewModel
    data class State(
        val isPlaying: Boolean = false,
        val progress: Long = 0,
        val progressPercent: Float = 0f,
        val displayProgress: String = "00:00",
        val duration: Long = 0L,
        val playbackState: PlaybackState = PlaybackState.Initial
    ) : Parcelable

    sealed class Action {
        object Load : Action()
        object Play : Action()
        object Pause : Action()
        object Stop : Action()
        object Previous : Action()
        object Next : Action()
        data class UpdateProgress(val newProgress: Float) : Action()
    }

    sealed class Event {
        object PlaybackFailed : Event()
    }
}
