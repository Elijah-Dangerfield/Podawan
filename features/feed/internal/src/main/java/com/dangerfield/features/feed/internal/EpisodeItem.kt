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
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.podcast.EpisodeImage
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
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.persistentListOf

@Composable
fun EpisodeItem(
    episode: DisplayableEpisode,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {},
    onDownloadClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
    onClickEpisode: () -> Unit = {},

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

        episode.releaseDate?.let {
            VerticalSpacerD500()

            Text(
                text = it,
                typography = PodawanTheme.typography.Label.L400,
            )
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

            Spacer(modifier = Modifier.weight(1f))


            if (episode.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(D800),
                    strokeWidth = D50,
                    color = PodawanTheme.colors.onBackground.color,
                )
            } else {
                IconButton(
                    icon = playingIcon,
                    onClick = {
                        if (episode.isPlaying) {
                            onPauseClicked()
                        } else {
                            onPlayClicked()
                        }
                    },
                    backgroundColor = PodawanTheme.colors.onBackground,
                    iconColor = PodawanTheme.colors.background,
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
                isPlaying = false,
                isLoading = false,
                isDownloaded = false,
                id = "",
                author = "Author Name"
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
                isPlaying = false,
                isLoading = false,
                isDownloaded = true,
                id = "",
                author = "Author Name"
            ),
        )
    }
}