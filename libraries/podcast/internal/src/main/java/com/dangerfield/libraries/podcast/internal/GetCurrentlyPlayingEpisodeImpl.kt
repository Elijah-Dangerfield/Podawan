package com.dangerfield.libraries.podcast.internal

import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.podcast.CurrentlyPlaying
import com.dangerfield.libraries.podcast.GetCurrentlyPlaying
import com.dangerfield.libraries.podcast.PodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import podawan.core.allOrNone
import podawan.core.checkInDebug
import podawan.core.logOnFailure
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
class GetCurrentlyPlayingImpl @Inject constructor(
    private val podcastRepository: PodcastRepository,
    private val playerStateRepository: PlayerStateRepository,
) : GetCurrentlyPlaying {

    /**
     * Returns the currently playing episode from the show or null if nothing is playing or the
     * episode cant be found
     */
    override fun invoke(): Flow<CurrentlyPlaying?> {

        return combine(
            flow { emit(podcastRepository.getPodcast().logOnFailure().getOrNull()) },
            playerStateRepository.getPlayerStateFlow().map { getCurrentlyPlayingDetails(it) }
        ) { podcastShow, (playingId, playingStatus) ->
            allOrNone(
                podcastShow,
                playingId,
                playingStatus
            ) { show, currentlyPlayingId, currentlyPlayingStatus ->
                // returns null if the currently playing id cant be found in the show episodes
                val currentlyPlayingShowEpisode = show.episodes
                    .firstOrNull { it.guid == currentlyPlayingId }
                    ?.toDisplayable(
                        show = show,
                        playback = currentlyPlayingStatus
                    )

                checkInDebug(currentlyPlayingShowEpisode != null) {
                    "Playback was active with an id not found in the show. Id was: $currentlyPlayingId"
                }

                currentlyPlayingShowEpisode?.let {
                    CurrentlyPlaying(episode = it,)
                }
            }
        }
    }
}
