package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import com.dangerfield.libraries.podcast.PodcastShow
import com.prof18.rssparser.RssParser
import kotlinx.coroutines.withContext
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

interface PodcastNetworkDataSource {
    suspend fun getPodcastWithRssFeedLink(rssFeedLink: String): Catching<PodcastShow>
}

@AutoBind
class DefaultPodcastNetworkDatasource @Inject constructor(
    private val rssParser: RssParser,
    private val dispatcherProvider: DispatcherProvider,
) : PodcastNetworkDataSource {
    override suspend fun getPodcastWithRssFeedLink(rssFeedLink: String): Catching<PodcastShow> {
        return Catching {
            withContext(dispatcherProvider.io) {
                val rssChannel = rssParser.getRssChannel(rssFeedLink)
                rssChannel.toDomain(rssFeedLink)
            }
        }
    }
}