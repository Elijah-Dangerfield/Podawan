package com.dangerfield.features.playback

import android.os.Parcelable
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize
import podawan.core.Catching

interface PlayerStateRepository {
    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getPlayerState(): PlayerState
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