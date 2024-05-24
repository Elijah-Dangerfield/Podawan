package com.dangerfield.features.playback

import android.os.Parcelable
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize
import podawan.core.Catching

interface PlayerStateRepository {
    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getPlayerState(): PlayerState

    fun isEpisodeCurrentlyPlaying(episodeId: String): Boolean
    suspend fun playEpisode(episodeId: String): Catching<Unit>
    fun pauseEpisode()
    fun seekTo(progressSeconds: Int)
}

@Parcelize
sealed class PlayerState : Parcelable {
    object None : PlayerState()
    data class Preparing(val episodeId: String) : PlayerState()
    data class Buffering(val episodeId: String, val progressSeconds: Int, val durationSeconds: Int) : PlayerState()
    data class ReadyToPlay(val episodeId: String, val progressSeconds: Int, val durationSeconds: Int) : PlayerState()
    data class Playing(val episodeId: String, val progressSeconds: Int, val durationSeconds: Int) : PlayerState()
    data class Paused(val episodeId: String, val progressSeconds: Int, val durationSeconds: Int) : PlayerState()
    data class Ended(val episodeId: String, val progressSeconds: Int, val durationSeconds: Int) : PlayerState()
    data class Error(val message: String) : PlayerState()
}

fun PlayerState.idToProgress(): Pair<String?, Int?> {
    return when (this) {
        is PlayerState.None -> null to null
        is PlayerState.Preparing -> episodeId to null
        is PlayerState.Buffering -> episodeId to progressSeconds
        is PlayerState.ReadyToPlay -> episodeId to progressSeconds
        is PlayerState.Playing -> episodeId to progressSeconds
        is PlayerState.Paused -> episodeId to progressSeconds
        is PlayerState.Ended -> episodeId to progressSeconds
        is PlayerState.Error -> null to null
    }
}

fun PlayerState.idToDuration(): Pair<String, Int>? {
    return when (this) {
        is PlayerState.None -> null
        is PlayerState.Preparing -> null
        is PlayerState.Buffering -> episodeId to durationSeconds
        is PlayerState.ReadyToPlay -> episodeId to durationSeconds
        is PlayerState.Playing -> episodeId to durationSeconds
        is PlayerState.Paused -> episodeId to durationSeconds
        is PlayerState.Ended -> episodeId to durationSeconds
        is PlayerState.Error -> null
    }
}

fun PlayerState.currentlyPlayingId(): String? {
    return when (this) {
        is PlayerState.None -> null
        is PlayerState.Preparing -> episodeId
        is PlayerState.Buffering -> episodeId
        is PlayerState.ReadyToPlay -> episodeId
        is PlayerState.Playing -> episodeId
        is PlayerState.Paused -> episodeId
        is PlayerState.Ended -> episodeId
        is PlayerState.Error -> null
    }
}