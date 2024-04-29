package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.EpisodeItunesData
import com.dangerfield.libraries.podcast.HeroImage
import com.dangerfield.libraries.podcast.ItunesChannelData
import com.dangerfield.libraries.podcast.ItunesOwner
import com.dangerfield.libraries.podcast.PodcastEpisode
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.storage.EpisodeItunesDataEntity
import com.dangerfield.libraries.podcast.storage.HeroImageEntity
import com.dangerfield.libraries.podcast.storage.ItunesChannelDataEntity
import com.dangerfield.libraries.podcast.storage.PodcastEpisodeEntity
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import podawan.core.ifNotEmpty

fun EpisodeItunesDataEntity.toDomain() =
    EpisodeItunesData(
        author = author?.ifNotEmpty(),
        subtitle = subtitle?.ifNotEmpty(),
        summary = summary?.ifNotEmpty(),
        duration = duration?.ifNotEmpty(),
        explicit = explicit?.ifNotEmpty(),
        image = image?.ifNotEmpty(),
        episodeType = episodeType?.ifNotEmpty(),
        season = season?.ifNotEmpty(),
        episode = episode?.ifNotEmpty(),
        keywords = keywords,
    )

fun PodcastEpisodeEntity.toDomain(
    itunesItemData: EpisodeItunesData?,
    showHeroImage: HeroImage?
) = PodcastEpisode(
    title = title?.ifNotEmpty(),
    description = description?.ifNotEmpty(),
    link = link?.ifNotEmpty(),
    pubDate = pubDate?.ifNotEmpty(),
    guid = guid,
    author = author?.ifNotEmpty(),
    content = content?.ifNotEmpty(),
    image = image?.ifNotEmpty(),
    audio = audio?.ifNotEmpty(),
    video = video?.ifNotEmpty(),
    sourceName = sourceName?.ifNotEmpty(),
    sourceUrl = sourceUrl?.ifNotEmpty(),
    categories = categories,
    itunesItemData = itunesItemData,
    commentsUrl = commentsUrl?.ifNotEmpty(),
    showHeroImage = showHeroImage
)

fun HeroImageEntity.toDomain() =
    HeroImage(
        title = title?.ifNotEmpty(),
        url = url?.ifNotEmpty(),
        link = link?.ifNotEmpty(),
        description = description
    )

fun ItunesChannelDataEntity.toDomain() =
    ItunesChannelData(
        author = author?.ifNotEmpty(),
        categories = categories,
        explicit = explicit?.ifNotEmpty(),
        image = image?.ifNotEmpty(),
        owner = ItunesOwner(
            email = ownerEmail?.ifNotEmpty(),
            name = ownerName
        ),
        subtitle = subtitle?.ifNotEmpty(),
        summary = summary?.ifNotEmpty(),
        type = type?.ifNotEmpty(),
        duration = duration?.ifNotEmpty(),
        keywords = keywords,
        newsFeedUrl = newsFeedUrl?.ifNotEmpty(),
    )

fun EpisodeItunesData.toEntity() = EpisodeItunesDataEntity(
    author = author?.ifNotEmpty(),
    subtitle = subtitle?.ifNotEmpty(),
    summary = summary?.ifNotEmpty(),
    duration = duration?.ifNotEmpty(),
    explicit = explicit?.ifNotEmpty(),
    image = image?.ifNotEmpty(),
    episodeType = episodeType?.ifNotEmpty(),
    season = season?.ifNotEmpty(),
    episode = episode?.ifNotEmpty(),
    keywords = keywords,
)

fun PodcastEpisode.toEntity(
    rssFeedLink: String,
    itunesItemDataId: Long?,
    showHeroImageId: Long?
) = PodcastEpisodeEntity(
    title = title?.ifNotEmpty(),
    description = description?.ifNotEmpty(),
    link = link?.ifNotEmpty(),
    pubDate = pubDate?.ifNotEmpty(),
    guid = guid,
    author = author?.ifNotEmpty(),
    content = content?.ifNotEmpty(),
    image = image?.ifNotEmpty(),
    audio = audio?.ifNotEmpty(),
    video = video?.ifNotEmpty(),
    sourceName = sourceName?.ifNotEmpty(),
    sourceUrl = sourceUrl?.ifNotEmpty(),
    categories = categories,
    itunesItemDataId = itunesItemDataId,
    showRssFeedLink = rssFeedLink,
    commentsUrl = commentsUrl?.ifNotEmpty(),
    showHeroImageId = showHeroImageId
)

fun HeroImage.toEntity() = HeroImageEntity(
    title = title?.ifNotEmpty(),
    url = url?.ifNotEmpty(),
    link = link?.ifNotEmpty(),
    description = description
)

fun ItunesChannelData.toEntity() = ItunesChannelDataEntity(
    author = author?.ifNotEmpty(),
    categories = categories,
    explicit = explicit?.ifNotEmpty(),
    image = image?.ifNotEmpty(),
    ownerName = owner?.name?.ifNotEmpty(),
    ownerEmail = owner?.email?.ifNotEmpty(),
    subtitle = subtitle?.ifNotEmpty(),
    summary = summary?.ifNotEmpty(),
    type = type?.ifNotEmpty(),
    duration = duration?.ifNotEmpty(),
    keywords = keywords,
    newsFeedUrl = newsFeedUrl?.ifNotEmpty(),
)

fun RssChannel.toDomain(rssFeedLink: String): PodcastShow {
    val heroImage = this.image?.let {
        HeroImage(
            title = it.title?.ifNotEmpty(),
            url = it.url?.ifNotEmpty(),
            link = it.link?.ifNotEmpty(),
            description = it.description
        )
    }
    return PodcastShow(
        title = this.title?.ifNotEmpty(),
        link = this.link?.ifNotEmpty(),
        description = this.description?.ifNotEmpty(),
        image = heroImage,
        lastBuildDate = this.lastBuildDate?.ifNotEmpty(),
        updatePeriod = this.updatePeriod?.ifNotEmpty(),
        items = this.items.mapIndexed { index, it ->

            val identifier = getIdentifier(it, items.size, index)

            PodcastEpisode(
                guid = identifier,
                title = it.title?.ifNotEmpty(),
                author = it.author?.ifNotEmpty(),
                link = it.link?.ifNotEmpty(),
                pubDate = it.pubDate?.ifNotEmpty(),
                description = it.description?.ifNotEmpty(),
                content = it.content?.ifNotEmpty(),
                image = it.image?.ifNotEmpty(),
                audio = it.audio?.ifNotEmpty(),
                video = it.video?.ifNotEmpty(),
                sourceName = it.sourceName?.ifNotEmpty(),
                sourceUrl = it.sourceUrl?.ifNotEmpty(),
                categories = it.categories,
                itunesItemData = it.itunesItemData?.let { itunesData ->
                    EpisodeItunesData(
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
                showHeroImage = heroImage
            )
        },
        itunesChannelData = this.itunesChannelData?.let {
            ItunesChannelData(
                author = it.author?.ifNotEmpty(),
                categories = it.categories,
                duration = it.duration?.ifNotEmpty(),
                explicit = it.explicit?.ifNotEmpty(),
                image = it.image?.ifNotEmpty(),
                keywords = it.keywords,
                newsFeedUrl = it.newsFeedUrl?.ifNotEmpty(),
                owner = it.owner?.let { owner ->
                    ItunesOwner(name = owner.name?.ifNotEmpty(), email = owner.email)
                },
                subtitle = it.subtitle?.ifNotEmpty(),
                summary = it.summary?.ifNotEmpty(),
                type = it.type?.ifNotEmpty(),

            )
        },
        rssFeedLink = rssFeedLink
    )
}

/**
 * Items are sorted by publish date, and naturally only have episodes added to the front of the list
 * this should make size - index a stable identifier to fallback on if all else fails.
 */
private fun getIdentifier(episode: RssItem, size: Int, index: Int): String {
    return  episode.guid ?: episode.title ?: episode.link ?: episode.pubDate ?: (size - index).toString()
}
