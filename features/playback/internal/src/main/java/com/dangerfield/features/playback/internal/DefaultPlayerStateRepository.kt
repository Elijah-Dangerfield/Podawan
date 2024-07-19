package com.dangerfield.features.playback.internal

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.dangerfield.features.playback.PlayerState
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.features.playback.currentlyPlayingId
import com.dangerfield.features.playback.idToDuration
import com.dangerfield.features.playback.idToProgress
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.dangerfield.libraries.podcast.PodcastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import podawan.core.Catching
import podawan.core.ifLinkFormat
import podawan.core.ignoreValue
import podawan.core.logOnFailure
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

@Singleton
@AutoBind
class DefaultPlayerStateRepository @Inject constructor(
    private val playbackHandler: PlaybackHandler,
    private val podcastRepository: PodcastRepository,
    @AppScope private val appScope: CoroutineScope
) : PlayerStateRepository {

    init {
        playbackHandler.playbackState.collectIn(appScope) {
            val (currentId, currentProgressSeconds) = it.idToProgress()

            if (currentId != null && currentProgressSeconds != null) {
                podcastRepository
                    .updateResumePoint(
                        id = currentId,
                        resumePoint = currentProgressSeconds.toDuration(SECONDS)
                    )
                    .logOnFailure()
            }
        }

        playbackHandler
            .playbackState
            .mapNotNull { it.idToDuration() }
            .filter { (_, duration) ->  duration > 0 }
            .distinctUntilChangedBy { (id, _) -> id }
            .collectIn(appScope) { (id, duration) ->
                podcastRepository.updateDuration(
                    id,
                    duration.toDuration(SECONDS)
                )
                    .logOnFailure()
            }
    }

    override fun getPlayerStateFlow(): Flow<PlayerState> = playbackHandler.playbackState

    override fun getPlayerState(): PlayerState = playbackHandler.playbackState.value

    override fun isEpisodeCurrentlyPlaying(episodeId: String): Boolean {
        return getPlayerState().currentlyPlayingId() == episodeId
    }

    override suspend fun playEpisode(episodeId: String): Catching<Unit> {
        return appScope.async {

            podcastRepository.getEpisode(episodeId)
                .onSuccess { episode ->
                    val imageToUse = episode.images.firstOrNull()

                    val mediaItem = MediaItem.Builder()
                        .setUri(episode.audio)
                        .setMediaId(episodeId)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setArtworkUri(Uri.parse(imageToUse))
                                .setAlbumTitle(episode.title)
                                .setDisplayTitle(episode.title)
                                .build()
                        ).build()

                    val currentlyPlayingId = getPlayerState().currentlyPlayingId()

                    if (currentlyPlayingId == episodeId) {
                        playbackHandler.onPlayerEvent(PlayerEvent.Play())
                    } else {
                        playbackHandler.prepareItem(mediaItem)
                        playbackHandler.onPlayerEvent(PlayerEvent.Play(seekTo = episode.resumePoint))
                    }
                }
                .ignoreValue()

        }.await()
    }


    override fun pauseEpisode() {
        playbackHandler.onPlayerEvent(PlayerEvent.Pause)
    }

    override fun seekTo(progressSeconds: Int) {
        playbackHandler.onPlayerEvent(PlayerEvent.Seek(progressSeconds))
    }
}