package com.dangerfield.features.feed.internal

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodeImage
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.CircularProgressIndicator
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.ClickableText
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.persistentListOf
import podawan.core.App

@Composable
fun EpisodeDetailsScreen(
    modifier: Modifier = Modifier,
    episode: DisplayableEpisode,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {},
    onDownloadClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onClickLink: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Screen(
        modifier,
        topBar = {
            Row {
                IconButton(
                    modifier = Modifier.padding(Dimension.D500),
                    icon = PodawanIcon.ArrowBack(""),
                    onClick = onNavigateBack
                )
            }
        },
    ) {
        Column(
            modifier =
            Modifier
                .padding(it)
                .padding(horizontal = Dimension.D500)
                .verticalScroll(scrollState)
                .fadingEdge(scrollState)
        ) {

            Row {
                EpisodeImage(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .clip(Radii.Card.shape)
                        .border(
                            2.dp,
                            PodawanTheme.colors.border.color,
                            Radii.Card.shape
                        ),
                    imageUrls = episode.imageUrls,
                )

                HorizontalSpacerD500()

                Column {
                    Text(
                        text = episode.title,
                        typography = PodawanTheme.typography.Heading.H700,
                    )

                    episode.author?.let { author ->
                        VerticalSpacerD500()

                        Text(
                            text = author,
                            typography = PodawanTheme.typography.Label.L500,
                        )
                    }

                    episode.releaseDate?.let {
                        VerticalSpacerD500()

                        Text(
                            text = it,
                            typography = PodawanTheme.typography.Label.L400,
                        )
                    }
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
                    size = IconButton.Size.Medium,
                )

                HorizontalSpacerD200()

                IconButton(
                    icon = PodawanIcon.Share(""),
                    onClick = onShareClicked,
                    size = IconButton.Size.Medium,
                )

                Spacer(modifier = Modifier.weight(1f))

                if (episode.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dimension.D1000),
                        strokeWidth = Dimension.D50,
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
                        size = IconButton.Size.Medium,
                    )
                }
            }

            VerticalSpacerD500()

            ClickableText(
                text = episode.description,
                onClickUrl = onClickLink,
                typography = PodawanTheme.typography.Body.B500,
            )

            VerticalSpacerD500()
        }
    }
}

@Composable
@Preview
private fun PreviewEpisodeDetailsScreen() {
    Preview {
        EpisodeDetailsScreen(
            episode = DisplayableEpisode(
                title = loremIpsum(3..10),
                releaseDate = "Dec 12, 2021",
                imageUrls = persistentListOf(),
                description = loremIpsum(50..100),
                isDownloaded = false,
                isPlaying = false,
                isLoading = false,
                id = "",
                author = "Author Name"
            ),
        )
    }
}

@Composable
@Preview
private fun PreviewEpisodeDetailsScreenStuffYouShouldKnow() {
    Preview(app = App.StuffYouShouldKnow) {
        EpisodeDetailsScreen(
            episode = DisplayableEpisode(
                title = loremIpsum(3..10),
                releaseDate = "Dec 12, 2021",
                imageUrls = persistentListOf(),
                description = loremIpsum(50..100),
                isPlaying = true,
                isLoading = false,
                isDownloaded = false,
                id = "",
                author = "Author Name"
            ),
        )
    }
}


