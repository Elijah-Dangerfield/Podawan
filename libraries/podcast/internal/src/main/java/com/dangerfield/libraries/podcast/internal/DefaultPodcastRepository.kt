package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.PodcastEpisode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.libraries.podcast.PodcastShow
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import oddoneout.core.AppConfiguration
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
class DefaultPodcastRepository @Inject constructor(
    private val rssParser: RssParser,
    private val appConfiguration: AppConfiguration
) : PodcastRepository {

    private val cachedPodcast = mutableMapOf<String, PodcastShow>()

    override suspend fun getPodcast(): Catching<PodcastShow> = Catching {
        if (!cachedPodcast.containsKey(appConfiguration.rssFeedLink)) {
            val rssChannel: RssChannel = rssParser.getRssChannel(appConfiguration.rssFeedLink)
            cachedPodcast[appConfiguration.rssFeedLink] = rssChannel.toPodcastShow()
        }

        cachedPodcast[appConfiguration.rssFeedLink]!!
    }

    override suspend fun getEpisode(id: String): Catching<PodcastEpisode> = Catching {
        if (!cachedPodcast.containsKey(appConfiguration.rssFeedLink)) {
            val rssChannel: RssChannel = rssParser.getRssChannel(appConfiguration.rssFeedLink)
            cachedPodcast[appConfiguration.rssFeedLink] = rssChannel.toPodcastShow()
        }

        val show = cachedPodcast[appConfiguration.rssFeedLink]!!

        show.items.first { it.guid == id }
    }
}