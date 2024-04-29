package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.PodcastEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.asLocalDateTime
import oddoneout.core.AppConfiguration
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

    override suspend fun getPodcast(): Catching<PodcastShow> {
        return podcastCacheDatasource.getPodcastWithRssFeedLink(rssLink)
            .recoverCatching {
                podcastNetworkDataSource.getPodcastWithRssFeedLink(rssLink)
                    .onSuccess { Catching { podcastCacheDatasource.savePodcast(it) } }
                    .getOrThrow()
            }
            .map { show ->
                show.copy(items = show.items.sortedByDescending { it.pubDate?.asLocalDateTime() })
            }
    }

    override suspend fun getEpisode(id: String): Catching<PodcastEpisode> = Catching {
        val show = getPodcast().getOrThrow()
        show.items.first { it.guid == id }
    }
}