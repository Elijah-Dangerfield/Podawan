package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.GetDisplayableEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AutoBind
class GetDisplayableEpisodeImpl @Inject constructor(
    private val podcastRepository: PodcastRepository
) : GetDisplayableEpisode {

    override suspend fun invoke(id: String): Catching<DisplayableEpisode> {
        return podcastRepository.getPodcast()
            .mapCatching { show ->
                val episode = show.episodes.first { it.guid == id }
                episode.toDisplayable(
                    playback = EpisodePlayback.None
                        (
                        progress = episode.resumePoint,
                        duration = episode.totalDuration ?: 0.seconds
                    )
                )
            }
    }
}