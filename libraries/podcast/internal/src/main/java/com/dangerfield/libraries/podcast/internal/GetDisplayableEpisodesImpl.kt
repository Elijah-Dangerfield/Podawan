package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback.None
import com.dangerfield.libraries.podcast.GetCurrentlyPlaying
import com.dangerfield.libraries.podcast.GetDisplayableEpisodes
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.firstOrNull
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AutoBind
class GetDisplayableEpisodesImpl @Inject constructor(
    private val podcastRepository: PodcastRepository
) : GetDisplayableEpisodes {
    /**
     * Fetches the podcast episodes
     */
    override suspend fun invoke(show: PodcastShow?): Catching<ImmutableList<DisplayableEpisode>> {
        return if (show != null) {
            Catching { show }
        } else {
            podcastRepository.getPodcast()
        }.map { podcastShow ->
            podcastShow.episodes.map {
                val playback = None(
                    progress = it.resumePoint,
                    duration = it.totalDuration ?: 0.seconds
                )

                it.toDisplayable(
                    show = podcastShow,
                    playback = playback
                )
            }.toPersistentList()
        }
    }
}



