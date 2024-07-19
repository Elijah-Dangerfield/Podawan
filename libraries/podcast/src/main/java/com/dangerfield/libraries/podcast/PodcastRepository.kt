package com.dangerfield.libraries.podcast

import podawan.core.Catching
import kotlin.time.Duration

interface PodcastRepository {
    suspend fun getPodcast(): Catching<PodcastShow>
    suspend fun getEpisode(id: String): Catching<Episode>
    suspend fun getEpisodes(ids: List<String>): Catching<List<Episode>>

    suspend fun updateResumePoint(id: String, resumePoint: Duration): Catching<Unit>
    suspend fun updateDuration(episodeId: String, duration: Duration): Catching<Unit>
}

