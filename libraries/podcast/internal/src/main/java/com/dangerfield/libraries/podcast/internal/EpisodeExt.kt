package com.dangerfield.libraries.podcast.internal

import com.dangerfield.features.playback.PlayerState
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.asLocalDateTime
import kotlinx.collections.immutable.toPersistentList
import podawan.core.ifLinkFormat
import podawan.core.ifNotEmpty
import podawan.core.removeHtmlTags
import java.time.format.DateTimeFormatter
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun getCurrentlyPlayingDetails(playerState: PlayerState): Pair<String?, EpisodePlayback?> {
    val currentlyPlayingEpisode = when (playerState) {
        is PlayerState.Playing -> playerState.episodeId to EpisodePlayback.Playing(
            progress = playerState.progressSeconds.toDuration(DurationUnit.SECONDS),
            duration = playerState.durationSeconds.toDuration(DurationUnit.SECONDS)
        )

        is PlayerState.Paused -> playerState.episodeId to EpisodePlayback.Paused(
            progress = playerState.progressSeconds.toDuration(DurationUnit.SECONDS),
            duration = playerState.durationSeconds.toDuration(DurationUnit.SECONDS)
        )

        is PlayerState.ReadyToPlay -> playerState.episodeId to EpisodePlayback.Paused(
            progress = playerState.progressSeconds.toDuration(DurationUnit.SECONDS),
            duration = playerState.durationSeconds.toDuration(DurationUnit.SECONDS)
        )

        is PlayerState.Preparing -> playerState.episodeId to EpisodePlayback.Loading()
        is PlayerState.Buffering -> playerState.episodeId to EpisodePlayback.Loading(
            progress = playerState.progressSeconds.toDuration(DurationUnit.SECONDS),
            duration = playerState.durationSeconds.toDuration(DurationUnit.SECONDS)
        )

        is PlayerState.Ended,
        is PlayerState.Error,
        PlayerState.None -> null to null
    }

    return currentlyPlayingEpisode
}

fun List<DisplayableEpisode>.updatedEpisode(
    predicate: (DisplayableEpisode) -> Boolean,
    update: (DisplayableEpisode) -> DisplayableEpisode
): List<DisplayableEpisode> {
    val updatedList = this.toMutableList()
    updatedList.forEachIndexed { index, ep ->
        if (predicate(ep)) {
            updatedList[index] = update(ep)
            return@forEachIndexed
        }
    }

    return updatedList.toList()
}

fun Episode.toDisplayable(
    playback: EpisodePlayback,
): DisplayableEpisode {
    val episode = this

    return DisplayableEpisode(
        title = episode.title.orEmpty().removeHtmlTags(),
        releaseDate = episode.readableDate(),
        imageUrls = images.toPersistentList(),
        description = episode.description.orEmpty().removeHtmlTags(),
        isDownloaded = false, // TODO hook up with playback repo for download state
        author = episode.author,
        id = episode.guid,
        playback = playback
    )
}

fun Episode.readableDate(): String? {
    val dateTime = this.pubDate?.asLocalDateTime()
    return dateTime?.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
}