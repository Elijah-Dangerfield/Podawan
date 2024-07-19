package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.asLocalDateTime
import podawan.core.AppConfiguration
import podawan.core.Catching
import podawan.core.debugSnackOnError
import podawan.core.ignoreValue
import podawan.core.logOnFailure
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration

@AutoBind
@Singleton
class DefaultPodcastRepository @Inject constructor(
    private val podcastCacheDatasource: PodcastCacheDatasource,
    private val podcastNetworkDataSource: PodcastNetworkDataSource,
    appConfiguration: AppConfiguration,
) : PodcastRepository {

    private val rssLink = appConfiguration.rssFeedLink

    override suspend fun getPodcast(): Catching<PodcastShow> {
       return podcastCacheDatasource
            .getPodcastWithRssFeedLink(rssLink)
            .debugSnackOnError { "Error getting podcast from cache" }
            .logOnFailure()
            .recoverCatching {
                podcastNetworkDataSource.getPodcastWithRssFeedLink(
                    rssLink,
                    getResumePoint = {
                        // when refreshing is enabled we will want to take episode playback data and pair it with the response episodes
                        null
                    }
                )
                    .onSuccess { Catching { podcastCacheDatasource.savePodcast(it) } }
                    .getOrThrow()
            }
            .map { show ->
                show.copy(episodes = show.episodes.sortedByDescending { it.pubDate?.asLocalDateTime() })
            }
    }

    override suspend fun getEpisode(id: String): Catching<Episode> = Catching {
        val show = getPodcast().getOrThrow()
        show.episodes.first { it.guid == id }
    }

    override suspend fun getEpisodes(ids: List<String>): Catching<List<Episode>> = Catching {
        val idSet = ids.toSet()
        val show = getPodcast().getOrThrow()
        show.episodes.filter { it.guid in idSet }
    }

    override suspend fun updateResumePoint(id: String, resumePoint: Duration): Catching<Unit> {
        return podcastCacheDatasource.updateResumePoint(id, resumePoint).ignoreValue()
    }

    override suspend fun updateDuration(episodeId: String, duration: Duration): Catching<Unit> {
        return podcastCacheDatasource.updateDuration(episodeId, duration).ignoreValue()
    }
}