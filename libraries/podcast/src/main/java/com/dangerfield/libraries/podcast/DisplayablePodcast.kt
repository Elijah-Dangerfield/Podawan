package com.dangerfield.libraries.podcast

import podawan.core.Catching
import podawan.core.removeHtmlTags
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class DisplayableEpisode(
    val title: String,
    val releaseDate: String,
    val imageUrl: String,
    val description: String,
    val fallbackImageUrl: String,
    val isPlaying: Boolean,
    val author: String?,
    val isDownloaded: Boolean,
    val id: String,
)

fun PodcastShow.getDisplayableEpisodes(): List<DisplayableEpisode> {
    return items.map { episode ->
        DisplayableEpisode(
            title = episode.title.orEmpty().removeHtmlTags(),
            releaseDate = episode.pubDate?.toReadableDate().orEmpty(),
            imageUrl = episode.link.takeIf { it?.isNotBlank() == true } ?: this.image?.url.orEmpty(),
            description = episode.description.orEmpty().removeHtmlTags(),
            fallbackImageUrl = image?.url.orEmpty(),
            isPlaying = false,
            isDownloaded = false,
            author = episode.author,
            id = episode.guid ?: title.hashCode().toString()
        )
    }
}

fun PodcastEpisode.toDisplayable(removeHtml: Boolean = true) = DisplayableEpisode(
    title = title.orEmpty().removeHtmlTags(),
    releaseDate = pubDate?.toReadableDate().orEmpty(),
    imageUrl = link.takeIf { it?.isNotBlank() == true } ?: fallbackImageUrl.orEmpty(),
    description = description.orEmpty().let { if (removeHtml) it.removeHtmlTags() else it },
    fallbackImageUrl = fallbackImageUrl.orEmpty(),
    isPlaying = false,
    isDownloaded = false,
    author = author,
    id = guid ?: title.hashCode().toString()
)

fun String.toReadableDate(): String {
    val dateFormats = listOf(
        DateTimeFormatter.RFC_1123_DATE_TIME, // Common in RSS feeds
        DateTimeFormatter.ISO_DATE_TIME,
        DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz") // Example for different locales
    )

    val dateTime = dateFormats.firstNotNullOfOrNull { format ->
       Catching { LocalDateTime.parse(this, format) }.getOrNull()
    }

    return dateTime?.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) ?: this
}