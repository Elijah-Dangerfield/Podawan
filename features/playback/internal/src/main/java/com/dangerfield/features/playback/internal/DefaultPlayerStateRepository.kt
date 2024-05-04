package com.dangerfield.features.playback.internal

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.features.playback.PlayerState
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.podcast.PodcastRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import podawan.core.Catching
import podawan.core.ifLinkFormat
import podawan.core.ignoreValue
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
@AutoBind
class DefaultPlayerStateRepository @Inject constructor(
    private val playbackHandler: PlaybackHandler,
    private val podcastRepository: PodcastRepository,
    @AppScope private val appScope: CoroutineScope
) : PlayerStateRepository {

    override fun getPlayerStateFlow(): Flow<PlayerState> = playbackHandler.playbackState

    override fun getPlayerState(): PlayerState = playbackHandler.playbackState.value

    override suspend fun playEpisode(episodeId: String): Catching<Unit> {
        return appScope.async {
            podcastRepository.getEpisode(episodeId)
                .onSuccess { episode ->
                    val imageToUse = episode.itunesItemData?.image?.ifLinkFormat()
                            ?: episode.image?.ifLinkFormat()
                            ?: episode.showHeroImage?.url?.ifLinkFormat()

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

                    playbackHandler.prepareItem(mediaItem)
                    playbackHandler.onPlayerEvent(PlayerEvent.Play(startingPoint = 0.seconds))
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