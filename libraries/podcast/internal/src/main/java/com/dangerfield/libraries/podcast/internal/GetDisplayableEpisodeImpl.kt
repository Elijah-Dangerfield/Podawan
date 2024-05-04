package com.dangerfield.libraries.podcast.internal

import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.GetDisplayableEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import podawan.core.Catching
import podawan.core.logOnFailure
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
class GetDisplayableEpisodeImpl @Inject constructor(
    private val playerStateRepository: PlayerStateRepository,
    private val podcastRepository: PodcastRepository
) : GetDisplayableEpisode {

    override fun invoke(id: String): Flow<Catching<DisplayableEpisode>> {
        return combine(
            flow { emit(podcastRepository.getPodcast()) },
            playerStateRepository.getPlayerStateFlow().distinctUntilChanged().map { getCurrentlyPlayingDetails(it) }
        ) { podcastResult, (currentlyPlayingId, currentlyPlayingStatus) ->

            Catching {
                val podcast = podcastResult.logOnFailure().getOrThrow()
                val matchingEpisode = podcast
                    .episodes
                    .first { it.guid == id }
                    .toDisplayable(
                        show = podcast,
                        isPlaying = id == currentlyPlayingId,
                        isLoading = id == currentlyPlayingId && currentlyPlayingStatus?.isLoading ?: false
                    )

                matchingEpisode
            }
        }
    }
}