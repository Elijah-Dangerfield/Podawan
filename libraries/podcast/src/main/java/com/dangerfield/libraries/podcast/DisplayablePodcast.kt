package com.dangerfield.libraries.podcast

import podawan.core.ifLinkFormat
import podawan.core.ifNotEmpty
import podawan.core.removeHtmlTags
import java.time.format.DateTimeFormatter

data class DisplayableEpisode(
    val title: String,
    val releaseDate: String?,
    val imageUrls: List<String>,
    val description: String,
    val isPlaying: Boolean,
    val author: String?,
    val isDownloaded: Boolean,
    val id: String,
)

fun PodcastShow.getDisplayableEpisodes(): List<DisplayableEpisode> {
    return items.map { episode ->
        DisplayableEpisode(
            title = episode.title.orEmpty().removeHtmlTags(),
            releaseDate = episode.readableDate(),
            imageUrls = listOfNotNull(
                episode.image?.ifNotEmpty(),
                episode.itunesItemData?.image?.ifNotEmpty(),
                image?.url?.ifNotEmpty(),
                itunesChannelData?.image?.ifNotEmpty()
            ),
            description = episode.description.orEmpty().removeHtmlTags(),
            isPlaying = false,
            isDownloaded = false,
            author = episode.author,
            id = episode.guid
        )
    }
}

fun PodcastShow.getDisplayableEpisode(id: String, removeHtml: Boolean = true): DisplayableEpisode? {
    return items
        .firstOrNull { it.guid == id }
        ?.let { episode ->
            DisplayableEpisode(
                title = episode.title.orEmpty().removeHtmlTags(),
                releaseDate = episode.readableDate(),
                imageUrls = listOfNotNull(
                    episode.itunesItemData?.image?.ifNotEmpty()?.ifLinkFormat(),
                    episode.image?.ifNotEmpty()?.ifLinkFormat(),
                    itunesChannelData?.image?.ifNotEmpty()?.ifLinkFormat(),
                    image?.url?.ifNotEmpty()?.ifLinkFormat(),
                ),
                description = if (removeHtml) episode.description.orEmpty()
                    .removeHtmlTags() else episode.description.orEmpty(),
                isPlaying = false,
                isDownloaded = false,
                author = episode.author,
                id = episode.guid
            )
        }
}

fun PodcastEpisode.readableDate(): String? {
    val dateTime = this.pubDate?.asLocalDateTime()
    return dateTime?.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}