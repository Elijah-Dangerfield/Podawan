package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.EpisodeItunesData
import com.dangerfield.libraries.podcast.HeroImage
import com.dangerfield.libraries.podcast.ItunesChannelData
import com.dangerfield.libraries.podcast.ItunesOwner
import com.dangerfield.libraries.podcast.PodcastEpisode
import com.dangerfield.libraries.podcast.PodcastShow
import com.prof18.rssparser.model.RssChannel

fun RssChannel.toPodcastShow(): PodcastShow {
    return PodcastShow(
        title = this.title,
        link = this.link,
        description = this.description,
        image = this.image?.let {
            HeroImage(
                title = it.title,
                url = it.url,
                link = it.link,
                description = it.description
            )
        },
        lastBuildDate = this.lastBuildDate,
        updatePeriod = this.updatePeriod,
        items = this.items.map {
            PodcastEpisode(
                guid = it.guid,
                title = it.title,
                author = it.author,
                link = it.link,
                pubDate = it.pubDate,
                description = it.description,
                content = it.content,
                image = it.image,
                audio = it.audio,
                video = it.video,
                sourceName = it.sourceName,
                sourceUrl = it.sourceUrl,
                categories = it.categories,
                itunesItemData = it.itunesItemData?.let { itunesData ->
                    EpisodeItunesData(
                        author = itunesData.author,
                        duration = itunesData.duration,
                        episode = itunesData.episode,
                        episodeType = itunesData.episodeType,
                        explicit = itunesData.explicit,
                        image = itunesData.image,
                        keywords = itunesData.keywords,
                        subtitle = itunesData.subtitle,
                        summary = itunesData.summary,
                        season = itunesData.season
                    )
                },
                commentsUrl = it.commentsUrl
            )
        },
        itunesChannelData = this.itunesChannelData?.let {
            ItunesChannelData(
                author = it.author,
                categories = it.categories,
                duration = it.duration,
                explicit = it.explicit,
                image = it.image,
                keywords = it.keywords,
                newsFeedUrl = it.newsFeedUrl,
                owner = it.owner?.let { owner ->
                    ItunesOwner(name = owner.name, email = owner.email)
                },
                subtitle = it.subtitle,
                summary = it.summary,
                type = it.type
            )
        }
    )
}