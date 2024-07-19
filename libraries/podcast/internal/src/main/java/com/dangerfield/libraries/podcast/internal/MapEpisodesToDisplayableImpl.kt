package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.MapEpisodesToDisplayable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AutoBind
class MapEpisodesToDisplayableImpl @Inject constructor(): MapEpisodesToDisplayable {

    override suspend fun invoke(episodes: List<Episode>): Catching<ImmutableList<DisplayableEpisode>> {
        return Catching {
            episodes.map {

                val playback = EpisodePlayback.None(
                    progress = it.resumePoint,
                    duration = it.totalDuration ?: 0.seconds
                )

                it.toDisplayable(
                    playback = playback
                )
            }.toImmutableList()
        }
    }
}