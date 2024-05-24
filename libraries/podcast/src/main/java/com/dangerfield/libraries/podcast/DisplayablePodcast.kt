package com.dangerfield.libraries.podcast

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Immutable
data class DisplayableEpisode(
    val id: String,
    val title: String,
    val releaseDate: String?,
    val imageUrls: ImmutableList<String>,
    val description: String,
    val author: String?,
    val isDownloaded: Boolean,
    val playback: EpisodePlayback
)

val DisplayableEpisode.isPlaying: Boolean
    get() = playback.isPlaying

val DisplayableEpisode.isPaused: Boolean
    get() = playback is EpisodePlayback.Paused

val DisplayableEpisode.isLoading: Boolean
    get() = playback.isLoading

val DisplayableEpisode.progress: Duration
    get() = playback.progress

val DisplayableEpisode.duration: Duration
    get() = playback.duration

@Immutable
data class CurrentlyPlaying(
    val episode: DisplayableEpisode,
) {
    fun copyWithPlayback( f: (EpisodePlayback) -> EpisodePlayback): CurrentlyPlaying {
        return this.copy(episode = episode.copy(playback = f(episode.playback)))
    }
}

@Immutable
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

fun Int.playerSecondsDisplay(): String = this.toDuration(DurationUnit.SECONDS).toDisplayString()

fun Duration.toDisplayString(): String  {
    return toComponents { hours, minutes, seconds, _ ->
         if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else
            "%02d:%02d".format(minutes, seconds)
    }
}