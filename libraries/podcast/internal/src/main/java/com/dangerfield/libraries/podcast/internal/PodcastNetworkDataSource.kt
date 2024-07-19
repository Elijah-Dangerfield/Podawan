package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.ItunesEpisodeData
import com.dangerfield.libraries.podcast.PodcastShow
import com.prof18.rssparser.RssParser
import kotlinx.coroutines.withContext
import podawan.core.Catching
import podawan.core.ifLinkFormat
import podawan.core.ifNotEmpty
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface PodcastNetworkDataSource {
    suspend fun getPodcastWithRssFeedLink(
        rssFeedLink: String,
        getResumePoint: (String) -> Duration?
    ): Catching<PodcastShow>
}

@AutoBind
class DefaultPodcastNetworkDatasource @Inject constructor(
    private val rssParser: RssParser,
    private val dispatcherProvider: DispatcherProvider,
) : PodcastNetworkDataSource {
    override suspend fun getPodcastWithRssFeedLink(
        rssFeedLink: String,
        getResumePoint: (String) -> Duration?
    ): Catching<PodcastShow> {
        return Catching {
            withContext(dispatcherProvider.io) {
                val rssChannel = rssParser.getRssChannel(rssFeedLink)
                rssChannel.toDomain(
                    rssFeedLink,
                    itemsToEpisodes = { items, heroImage ->
                        items.mapIndexed { index, it ->

                            val identifier = getIdentifier(it, items.size, index)

                            Episode(
                                guid = identifier,
                                title = it.title?.ifNotEmpty(),
                                author = it.author?.ifNotEmpty() ?: rssChannel.itunesChannelData?.author?.ifNotEmpty() ?: rssChannel.title,
                                link = it.link?.ifNotEmpty(),
                                pubDate = it.pubDate?.ifNotEmpty(),
                                description = it.description?.ifNotEmpty(),
                                content = it.content?.ifNotEmpty(),
                                images = listOfNotNull(
                                    it.image?.ifNotEmpty()?.ifLinkFormat(),
                                    it.itunesItemData?.image?.ifNotEmpty()?.ifLinkFormat(),
                                    heroImage?.url?.ifLinkFormat(),
                                    rssChannel.itunesChannelData?.image?.ifNotEmpty()?.ifLinkFormat()
                                ),
                                audio = it.audio?.ifNotEmpty(),
                                video = it.video?.ifNotEmpty(),
                                sourceName = it.sourceName?.ifNotEmpty(),
                                sourceUrl = it.sourceUrl?.ifNotEmpty(),
                                categories = it.categories,
                                itunesItemData = it.itunesItemData?.let { itunesData ->
                                    ItunesEpisodeData(
                                        author = itunesData.author?.ifNotEmpty(),
                                        duration = itunesData.duration?.ifNotEmpty(),
                                        episode = itunesData.episode?.ifNotEmpty(),
                                        episodeType = itunesData.episodeType?.ifNotEmpty(),
                                        explicit = itunesData.explicit?.ifNotEmpty(),
                                        image = itunesData.image?.ifNotEmpty(),
                                        keywords = itunesData.keywords,
                                        subtitle = itunesData.subtitle?.ifNotEmpty(),
                                        summary = itunesData.summary?.ifNotEmpty(),
                                        season = itunesData.season
                                    )
                                },
                                commentsUrl = it.commentsUrl?.ifNotEmpty(),
                                resumePoint = getResumePoint(identifier) ?: 0.seconds,
                                totalDuration = null, // we don't know the total duration yet
                                showRssFeedLink = rssFeedLink
                            )
                        }
                    }
                )
            }
        }
    }
}