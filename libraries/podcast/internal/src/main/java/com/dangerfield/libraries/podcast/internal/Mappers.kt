package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.podcast.ItunesEpisodeData
import com.dangerfield.libraries.podcast.HeroImage
import com.dangerfield.libraries.podcast.ItunesShowData
import com.dangerfield.libraries.podcast.ItunesOwner
import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.storage.EpisodeItunesDataEntity
import com.dangerfield.libraries.podcast.storage.HeroImageEntity
import com.dangerfield.libraries.podcast.storage.ItunesChannelDataEntity
import com.dangerfield.libraries.podcast.storage.PodcastEpisodeEntity
import com.prof18.rssparser.model.RssChannel
import com.prof18.rssparser.model.RssItem
import podawan.core.ifNotEmpty
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun EpisodeItunesDataEntity.toDomain() =
    ItunesEpisodeData(
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
    itunesItemData: ItunesEpisodeData?,
    showHeroImage: HeroImage?
) = Episode(
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
    showHeroImage = showHeroImage,
    resumePoint = resumePointSeconds.toDuration(DurationUnit.SECONDS),
    totalDuration = totalDurationSeconds.toDuration(DurationUnit.SECONDS),
    showRssFeedLink = showRssFeedLink
)

fun HeroImageEntity.toDomain() =
    HeroImage(
        title = title?.ifNotEmpty(),
        url = url?.ifNotEmpty(),
        link = link?.ifNotEmpty(),
        description = description
    )

fun ItunesChannelDataEntity.toDomain() =
    ItunesShowData(
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

fun ItunesEpisodeData.toEntity() = EpisodeItunesDataEntity(
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

fun Episode.toEntity(
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
    showHeroImageId = showHeroImageId,
    resumePointSeconds = resumePoint.inWholeSeconds.toInt(),
    totalDurationSeconds = totalDuration?.inWholeSeconds?.toInt() ?: 0
)

fun HeroImage.toEntity() = HeroImageEntity(
    title = title?.ifNotEmpty(),
    url = url?.ifNotEmpty(),
    link = link?.ifNotEmpty(),
    description = description
)

fun ItunesShowData.toEntity() = ItunesChannelDataEntity(
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

fun RssChannel.toDomain(
    rssFeedLink: String,
    itemsToEpisodes: (List<RssItem>, HeroImage?) -> List<Episode>
): PodcastShow {
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
        heroImage = heroImage,
        lastBuildDate = this.lastBuildDate?.ifNotEmpty(),
        updatePeriod = this.updatePeriod?.ifNotEmpty(),
        episodes = itemsToEpisodes(this.items, heroImage),
        itunesShowData = this.itunesChannelData?.let {
            ItunesShowData(
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
fun getIdentifier(episode: RssItem, size: Int, index: Int): String {
    return  episode.guid ?: episode.title ?: episode.link ?: episode.pubDate ?: (size - index).toString()
}
