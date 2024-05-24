package com.dangerfield.features.feed.internal

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodeImage
import com.dangerfield.libraries.podcast.EpisodePlayback.Playing
import com.dangerfield.libraries.podcast.isLoading
import com.dangerfield.libraries.podcast.isPaused
import com.dangerfield.libraries.podcast.isPlaying
import com.dangerfield.libraries.ui.Dimension.D1500
import com.dangerfield.libraries.ui.Dimension.D50
import com.dangerfield.libraries.ui.Dimension.D800
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.rememberRipple
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.CircularProgressIndicator
import com.dangerfield.ui.components.LinearProgressIndicator
import com.dangerfield.ui.components.ProgressRow
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.persistentListOf
import podawan.core.allOrNone

@Composable
fun EpisodeItem(
    episode: DisplayableEpisode,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {},
    onDownloadClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
    onClickEpisode: () -> Unit = {},
    onAddToPlaylistClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier.clickable(
            indication = rememberRipple(),
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClickEpisode
        )
    ) {
        Row {
            EpisodeImage(
                modifier = Modifier
                    .width(D1500)
                    .clip(Radii.Card.shape)
                    .border(
                        2.dp,
                        PodawanTheme.colors.border.color,
                        Radii.Card.shape
                    ),
                imageUrls = episode.imageUrls,
            )

            HorizontalSpacerD500()

            Text(
                text = episode.title,
                typography = PodawanTheme.typography.Heading.H700,
            )
        }

        VerticalSpacerD500()

        Text(
            text = episode.description,
            maxLines = 5,
            typography = PodawanTheme.typography.Body.B400,
            colorResource = PodawanTheme.colors.textSecondary
        )

        VerticalSpacerD500()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            episode.releaseDate?.let {
                VerticalSpacerD500()

                Text(
                    text = it,
                    typography = PodawanTheme.typography.Label.L400,
                )
            }


            HorizontalSpacerD500()

            allOrNone(
                episode.playback.progress.inWholeSeconds,
                episode.playback.duration.inWholeSeconds.takeIf { it > 0 }
            ) { progress, duration ->
                LinearProgressIndicator(
                    modifier = Modifier.clip(Radii.Card.shape),
                    trackColor = PodawanTheme.colors.textDisabled.color,
                    progress = progress.toFloat() / duration,
                )
            }
        }

        VerticalSpacerD500()

        val playingIcon = if (episode.isPlaying) PodawanIcon.Pause("") else PodawanIcon.Play("")
        val downloadIcon =
            if (episode.isDownloaded) PodawanIcon.Check("") else PodawanIcon.ArrowCircleDown("")
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(
                icon = downloadIcon,
                onClick = onDownloadClicked,
                size = IconButton.Size.Small,
            )

            HorizontalSpacerD200()

            IconButton(
                icon = PodawanIcon.Share(""),
                onClick = onShareClicked,
                size = IconButton.Size.Small,
            )

            HorizontalSpacerD200()

            IconButton(
                icon = PodawanIcon.Add(""),
                onClick = onAddToPlaylistClicked,
                size = IconButton.Size.Small,
            )


            Spacer(modifier = Modifier.weight(1f))


            if (episode.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(D800),
                    strokeWidth = D50,
                    color = PodawanTheme.colors.onBackground.color,
                )
            } else {
                val isCurrentlyPlaying = episode.isPlaying || episode.isPaused || episode.isLoading

                val backgroundColor = if (isCurrentlyPlaying) {
                    PodawanTheme.colors.accent
                } else {
                    PodawanTheme.colors.onBackground
                }

                val iconColor = if (isCurrentlyPlaying) {
                    PodawanTheme.colors.onAccent
                } else {
                    PodawanTheme.colors.background
                }

                IconButton(
                    icon = playingIcon,
                    onClick = {
                        if (episode.isPlaying) {
                            onPauseClicked()
                        } else {
                            onPlayClicked()
                        }
                    },
                    backgroundColor = backgroundColor,
                    iconColor = iconColor,
                    size = IconButton.Size.Small,
                )
            }
        }

        VerticalSpacerD500()
    }
}


@Composable
@Preview
private fun PreviewEpisodeItem() {
    Preview {
        EpisodeItem(
            episode = DisplayableEpisode(
                title = com.dangerfield.libraries.ui.preview.loremIpsum(3..10),
                releaseDate = "Dec 12, 2021",
                imageUrls = persistentListOf(),
                description = com.dangerfield.libraries.ui.preview.loremIpsum(20..30),
                isDownloaded = false,
                id = "",
                author = "Author Name",
               playback = Playing()
            ),
        )
    }
}

@Composable
@Preview
private fun PreviewEpisodeItemPlaying() {
    com.dangerfield.libraries.ui.preview.Preview {
        EpisodeItem(
            episode = DisplayableEpisode(
                title = com.dangerfield.libraries.ui.preview.loremIpsum(3..10),
                releaseDate = "Dec 12, 2021",
                imageUrls = persistentListOf(),
                description = com.dangerfield.libraries.ui.preview.loremIpsum(20..30),
                playback = Playing(),
                isDownloaded = true,
                id = "",
                author = "Author Name",
            ),
        )
    }
}