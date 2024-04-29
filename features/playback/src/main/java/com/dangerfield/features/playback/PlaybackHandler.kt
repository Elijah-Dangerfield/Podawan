package com.dangerfield.features.playback

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

class PlaybackHandler @Inject constructor(
    private val player: ExoPlayer
) : Player.Listener {

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Initial)
    val playbackState = _playbackState.asStateFlow()

    private var job: Job? = null

    init {
        player.addListener(this)
        job = Job()
    }

    fun addMediaItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        player.setMediaItems(mediaItemList)
        player.prepare()
    }

    suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Previous -> player.seekToPrevious()
            PlayerEvent.Next -> player.seekToNext()
            PlayerEvent.Play -> {
                player.play()
                _playbackState.value = PlaybackState.Playing(isPlaying = true)
                startProgressUpdate()
            }

            PlayerEvent.Pause -> {
                player.pause()
                stopProgressUpdate()
            }

            PlayerEvent.Stop -> {
                stopProgressUpdate()
                player.stop()
            }

            is PlayerEvent.UpdateProgress -> player.seekTo((player.duration * playerEvent.newProgress).toLong())
        }
    }

    @SuppressLint("SwitchIntDef")
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _playbackState.value =
                PlaybackState.Buffering(player.currentPosition)

            ExoPlayer.STATE_READY -> _playbackState.value =
                PlaybackState.Ready(player.duration)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _playbackState.value = PlaybackState.Playing(isPlaying = isPlaying)
        if (isPlaying) {
            // TODO change this I think
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            // TODO investiage if 500 is too frequent
            delay(500)
            _playbackState.value = PlaybackState.Progress(player.currentPosition)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Timber.e(error)
        _playbackState.value =
            PlaybackState.Error(error.message ?: "Error happened while playing")
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _playbackState.value = PlaybackState.Playing(isPlaying = false)
    }
}

sealed class PlayerEvent {
    object Play : PlayerEvent()
    object Pause : PlayerEvent()
    object Previous : PlayerEvent()
    object Next : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}


@Parcelize
sealed class PlaybackState : Parcelable {
    object Initial : PlaybackState()
    data class Ready(val duration: Long) : PlaybackState()
    data class Progress(val progress: Long) : PlaybackState()
    data class Buffering(val progress: Long) : PlaybackState()
    data class Playing(val isPlaying: Boolean) : PlaybackState()
    data class Error(val message: String) : PlaybackState()
}