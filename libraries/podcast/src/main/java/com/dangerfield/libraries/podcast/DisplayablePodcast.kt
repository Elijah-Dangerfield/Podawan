package com.dangerfield.libraries.podcast

import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


//TODO so im going to have the idea of a an episode and an episodes playback state be completely seperate.
// maybe CurrentlyPlayingEpisode is a good entity to have and this can be a constatnt updating object.
// the view model of the feed and listen to changes to update its list but only update on episdoe changes and playback status change ,
// not on every playback state change.
data class DisplayableEpisode(
    val id: String,
    val title: String,
    val releaseDate: String?,
    val imageUrls: ImmutableList<String>,
    val description: String,
    val author: String?,
    val isDownloaded: Boolean,
    val isPlaying: Boolean,
    val isLoading: Boolean,
)

data class CurrentlyPlayingEpisode(
    val episode: DisplayableEpisode,
    val episodePlayback: EpisodePlayback
)

sealed class EpisodePlayback(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val progress: Duration = 0.seconds,
    val duration: Duration = 0.seconds,
    val progressString: String = progress.toDisplayString(),
    val durationString: String = duration.toDisplayString(),
    val elapsedString: String = duration.minus(progress).toDisplayString()
) {
    class Playing(
        progress: Duration = 0.seconds,
        duration: Duration = 0.seconds
    ) : EpisodePlayback(
        isPlaying = true,
        isLoading = false,
        progress = progress,
        duration = duration
    )

    class Paused(
        progress: Duration = 0.seconds,
        duration: Duration = 0.seconds
    ) : EpisodePlayback(
        isPlaying = false,
        isLoading = false,
        progress = progress,
        duration = duration
    )

    class Loading(
        progress: Duration = 0.seconds,
        duration: Duration = 0.seconds
    ) : EpisodePlayback(
        isPlaying = false,
        isLoading = true,
        progress = progress,
        duration = duration
    )

    class None(
        progress: Duration = 0.seconds,
        duration: Duration = 0.seconds
    ) : EpisodePlayback(
        isPlaying = false,
        isLoading = false,
        progress = progress,
        duration = duration
    )
}

fun Duration.toDisplayString(): String  {
    return toComponents { hours, minutes, seconds, _ ->
         if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else
            "%02d:%02d".format(minutes, seconds)
    }
}