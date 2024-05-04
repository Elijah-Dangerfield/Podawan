package com.dangerfield.features.playback.internal.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.podcast.CurrentlyPlayingEpisode
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.EpisodeImage
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD1400
import com.dangerfield.libraries.ui.elevation
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.Screen
import com.dangerfield.libraries.ui.LocalContentColor
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.ui.components.CircularProgressIndicator
import com.dangerfield.ui.components.Slider
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheet
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetState
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetValue
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import com.kmpalette.DominantColorState
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Composable
fun PlayerScreen(
    episode: CurrentlyPlayingEpisode,
    bottomSheetState: BottomSheetState,
    onPause: (CurrentlyPlayingEpisode) -> Unit,
    onPlay: (CurrentlyPlayingEpisode) -> Unit,
    onBackClicked: () -> Unit,
    onSeek: (Float) -> Unit
) {

    var dominantColorState by remember {
        mutableStateOf<DominantColorState<ImageBitmap>?>(null)
    }

    BottomSheet(
        onDismissRequest = onBackClicked,
        showDragHandle = false,
        state = bottomSheetState
    ) {

        Screen(
            containerColor = dominantColorState?.color ?: PodawanTheme.colors.background.color,
            contentColor = dominantColorState?.onColor ?: PodawanTheme.colors.onBackground.color,
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(it)
                    .padding(horizontal = Dimension.D1000),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                VerticalSpacerD500()

                TopBar(onBackClicked, episode.episode)

                VerticalSpacerD1200()

                EpisodeImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .elevation(Elevation.AppBar, shape = Radii.Card.shape),
                    imageUrls = episode.episode.imageUrls,
                    onDominantColorDetected = { dominantColorState = it }
                )

                VerticalSpacerD1400()

                EpisodeInfo(episode.episode)

                VerticalSpacerD1200()

                ProgressSlider(
                    episode = episode,
                    onSeek = onSeek
                )

                VerticalSpacerD1200()

                PlaybackControls(
                    episode = episode,
                    onPause = onPause,
                    onPlay = onPlay
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    onBackClicked: () -> Unit,
    episode: DisplayableEpisode
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            icon = PodawanIcon.ArrowDown(""),
            onClick = onBackClicked
        )

        Text(
            text = episode.author ?: episode.title,
            typography = PodawanTheme.typography.Label.L600.Bold
        )

        IconButton(
            icon = PodawanIcon.MoreVert(""),
            onClick = {}
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun EpisodeInfo(episode: DisplayableEpisode) {

    val title by remember {
        derivedStateOf { episode.title  }
    }
    val author by remember {
        derivedStateOf { episode.author  }
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(),
        text = title,
        typography = PodawanTheme.typography.Heading.H700,
        maxLines = 1,
        textAlign = TextAlign.Center
    )

    VerticalSpacerD500()

    author?.let { nonNullAuthor ->
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            text = nonNullAuthor,
            typography = PodawanTheme.typography.Label.L600,
            maxLines = 1,
            colorResource = LocalContentColor.current.withAlpha(0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProgressSlider(
    episode: CurrentlyPlayingEpisode,
    onSeek: (Float) -> Unit
) {
    
    // TODO use on value change finished, create mutable state that follow progress but can be updates by the dragging
    Slider(
        value = episode.episodePlayback.progress.inWholeSeconds.toFloat(),
        valueRange = 0f..episode.episodePlayback.duration.inWholeSeconds.toFloat(),
        onValueChange = { onSeek(it) }
    )

    VerticalSpacerD500()

    Row(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = episode.episodePlayback.progressString,
            typography = PodawanTheme.typography.Label.L600,
            colorResource = LocalContentColor.current.withAlpha(0.6f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = episode.episodePlayback.elapsedString,
            typography = PodawanTheme.typography.Label.L600,
            colorResource = LocalContentColor.current.withAlpha(0.6f)
        )
    }
}

@Composable
private fun PlaybackControls(
    modifier : Modifier = Modifier,
    episode: CurrentlyPlayingEpisode,
    onPause: (CurrentlyPlayingEpisode) -> Unit,
    onPlay: (CurrentlyPlayingEpisode) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            modifier = Modifier.weight(1f),
            text = "1x",
            maxLines = 1,
            textAlign = TextAlign.Center,
            typography = PodawanTheme.typography.Label.L700.Bold
        )

        IconButton(
            modifier = Modifier.weight(1f),
            icon = PodawanIcon.Rewind10Seconds(""),
            size = IconButton.Size.Large,
            onClick = {}
        )

        val playingIcon =
            if (episode.episodePlayback.isPlaying) PodawanIcon.Pause("") else PodawanIcon.Play(
                ""
            )

        HorizontalSpacerD500()

        if (episode.episodePlayback.isLoading) {
            CircularProgressIndicator()
        } else {
            IconButton(
                modifier = Modifier.weight(1f),
                backgroundColor = LocalContentColor.current,
                iconColor = LocalContentColor.current.onColorResource,
                size = IconButton.Size.Largest,
                icon = playingIcon,
                onClick = {
                    if (episode.episodePlayback.isPlaying) {
                        onPause(episode)
                    } else {
                        onPlay(episode)
                    }
                }
            )
        }

        HorizontalSpacerD500()

        IconButton(
            modifier = Modifier.weight(1f),
            icon = PodawanIcon.FastForward10Seconds(""),
            size = IconButton.Size.Large,
            onClick = {}
        )

        IconButton(
            modifier = Modifier.weight(1f),
            icon = PodawanIcon.Timer(""),
            size = IconButton.Size.Medium,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewPlayerControls() {
    Preview {
        PlaybackControls(
            modifier = Modifier.padding(24.dp),
            CurrentlyPlayingEpisode(
                DisplayableEpisode(
                    id = "1",
                    title = "Healing",
                    releaseDate = "2021-08-01",
                    imageUrls = persistentListOf(),
                    description = "Emma Hinkley",
                    author = "Emma Hinkley",
                    isDownloaded = false,
                    isPlaying = false,
                    isLoading = false
                ),
                episodePlayback = EpisodePlayback.None()
            ),
            onPause = {},
            onPlay = {}
        )
    }
}

@Preview
@Composable
private fun PreviewPlayerScreen() {
    Preview {

        PlayerScreen(
            CurrentlyPlayingEpisode(
                DisplayableEpisode(
                    id = "1",
                    title = "Healing",
                    releaseDate = "2021-08-01",
                    imageUrls = persistentListOf(),
                    description = "Emma Hinkley",
                    author = "Emma Hinkley",
                    isDownloaded = false,
                    isPlaying = false,
                    isLoading = false
                ),
                episodePlayback = EpisodePlayback.None()
            ),
            onBackClicked = {},
            bottomSheetState = rememberBottomSheetState(initialState = BottomSheetValue.Expanded),
            onPause = {},
            onPlay = {},
            onSeek = {}
        )
    }
}