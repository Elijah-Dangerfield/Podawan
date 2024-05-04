package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.asLocalDateTime
import podawan.core.AppConfiguration
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
class DefaultPodcastRepository @Inject constructor(
    private val podcastCacheDatasource: PodcastCacheDatasource,
    private val podcastNetworkDataSource: PodcastNetworkDataSource,
    appConfiguration: AppConfiguration,
) : PodcastRepository {

    private val rssLink = appConfiguration.rssFeedLink
    // TODO add caching strategy for refreshing and determining staleness.
    private val inMemoryCache = mutableMapOf<String, PodcastShow>()

    override suspend fun getPodcast(): Catching<PodcastShow> {
        return if (inMemoryCache.containsKey(rssLink)) {
            Catching { inMemoryCache.getValue(rssLink) }
        } else {
            podcastCacheDatasource.getPodcastWithRssFeedLink(rssLink)
                .recoverCatching {
                    podcastNetworkDataSource.getPodcastWithRssFeedLink(rssLink)
                        .onSuccess { Catching { podcastCacheDatasource.savePodcast(it) } }
                        .getOrThrow()
                }
                .map { show ->
                    val shortedShow = show.copy(episodes = show.episodes.sortedByDescending { it.pubDate?.asLocalDateTime() })
                    inMemoryCache[rssLink] = shortedShow
                    shortedShow
                }
        }
    }

    override suspend fun getEpisode(id: String): Catching<Episode> = Catching {
        val show = getPodcast().getOrThrow()
        show.episodes.first { it.guid == id }
    }
}