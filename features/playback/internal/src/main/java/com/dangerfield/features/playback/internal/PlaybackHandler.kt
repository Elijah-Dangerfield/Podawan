package com.dangerfield.features.playback.internal

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.dangerfield.features.playback.PlayerState
import com.dangerfield.features.playback.PlayerState.Playing
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import com.dangerfield.libraries.coreflowroutines.collectIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import podawan.core.allOrNone
import timber.log.Timber
import kotlin.time.Duration
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

/**
 * Handles playback of media and exposes the playback state
 * The playback repo can act on this state to update persisted entities such as the playback
 * progress of an item.
 */
@Singleton
class PlaybackHandler @Inject constructor(
    private val player: ExoPlayer,
    private val dispatcherProvider: DispatcherProvider,
    @AppScope private val appScope: CoroutineScope,
) : Player.Listener {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.None)
    val playbackState = _playerState.asStateFlow()
    private val playerScope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)

    init {
        playbackState.collectIn(playerScope) {
            Timber.i("Playback Log. Exposed Playback state: $it")
        }
    }

    private var progressUpdateJob: Job? = null

    private val currentlyPlayingItem: CurrentlyPlayingItem?
        get() = allOrNone(
            player.currentMediaItem,
            player.duration,
            player.currentPosition
        ) { mediaItem, durationMillis, progressMillis ->

            CurrentlyPlayingItem(
                mediaItem = mediaItem,
                durationSeconds = durationMillis.coerceAtLeast(0).toInt().div(1000),
                progressSeconds = progressMillis.coerceAtLeast(0).toInt().div(1000)
            )
        }

    init {
        player.addListener(this)
    }

    fun prepareItem(mediaItem: MediaItem, playWhenReady: Boolean = false) {
        playerScope.launch {
            player.playWhenReady = playWhenReady
            player.setMediaItem(mediaItem)
            player.prepare()
        }
    }

    private fun startProgressTimer() {
        progressUpdateJob = appScope.launch {
            while (isActive) {

                _playerState.update {
                    if (it is Playing) {
                        it.copy(progressSeconds = it.progressSeconds + 1)
                    } else {
                        it
                    }
                }

                delay(1000)
            }
        }
    }

    private fun stopProgressTimer() {
        playerScope.launch {
            progressUpdateJob?.cancelAndJoin()
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        Timber.i("Playback Log. Is playing changed: $isPlaying. Currently playing item: $currentlyPlayingItem")

        if (isPlaying) {
            currentlyPlayingItem?.let {
                _playerState.value = Playing(
                    episodeId = it.mediaItem.mediaId,
                    progressSeconds = it.progressSeconds,
                    durationSeconds = it.durationSeconds
                )
            }
            startProgressTimer()
        } else {
            currentlyPlayingItem?.let {
                _playerState.value = PlayerState.Paused(
                    episodeId = it.mediaItem.mediaId,
                    progressSeconds = it.progressSeconds,
                    durationSeconds = it.durationSeconds
                )
            }
            stopProgressTimer()
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        _playerState.value = when (playbackState) {
            Player.STATE_BUFFERING -> {
                Timber.i("Playback Log. State: BUFFERING. Currently playing item: $currentlyPlayingItem")
                currentlyPlayingItem?.let {
                    PlayerState.Buffering(
                        episodeId = it.mediaItem.mediaId,
                        progressSeconds = it.progressSeconds,
                        durationSeconds = it.durationSeconds
                    )
                } ?: PlayerState.Error("Buffering but no item is playing")
            }

            Player.STATE_ENDED -> {
                Timber.i("Playback Log. State: ENDED. Currently playing item: $currentlyPlayingItem")
                currentlyPlayingItem?.let {
                    PlayerState.Ended(
                        episodeId = it.mediaItem.mediaId,
                        progressSeconds = it.progressSeconds,
                        durationSeconds = it.durationSeconds
                    )
                } ?: PlayerState.Error("Ended but no item is playing")
            }

            Player.STATE_IDLE -> {
                Timber.i("Playback Log. State: IDLE. Currently playing item: $currentlyPlayingItem")
                PlayerState.None
            }

            Player.STATE_READY -> {
                Timber.i("Playback Log. State: READY. Currently playing item: $currentlyPlayingItem")
                currentlyPlayingItem?.let {
                    if (player.playWhenReady) {
                        Playing(
                            episodeId = it.mediaItem.mediaId,
                            progressSeconds = it.progressSeconds,
                            durationSeconds = it.durationSeconds
                        )
                    } else {
                        PlayerState.ReadyToPlay(
                            episodeId = it.mediaItem.mediaId,
                            progressSeconds = it.progressSeconds,
                            durationSeconds = it.durationSeconds
                        )
                    }

                } ?: PlayerState.Error("Ready but no item is playing")
            }

            else -> _playerState.value
        }
    }

    fun onPlayerEvent(playerEvent: PlayerEvent) {
        Timber.i("Playback Log. Player event: $playerEvent")
        playerScope.launch {
            when (playerEvent) {
                is PlayerEvent.Play -> {
                    player.seekTo(playerEvent.startingPoint.inWholeMilliseconds)
                    player.play()
                }

                PlayerEvent.Pause -> player.pause()
                is PlayerEvent.Seek -> {
                    player.seekTo(playerEvent.seekSeconds.toDuration(SECONDS).inWholeMilliseconds)
                }
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Timber.i("Playback Log. PLAYER ERROR")
        Timber.e(error)
        _playerState.value =
            PlayerState.Error(error.message ?: "Error happened while playing")
    }

    private data class CurrentlyPlayingItem(
        val mediaItem: MediaItem,
        val durationSeconds: Int,
        val progressSeconds: Int,
    )
}

sealed class PlayerEvent {
    data class Play(val startingPoint: Duration) : PlayerEvent()
    data class Seek(val seekSeconds: Int) : PlayerEvent()
    object Pause : PlayerEvent()

}
