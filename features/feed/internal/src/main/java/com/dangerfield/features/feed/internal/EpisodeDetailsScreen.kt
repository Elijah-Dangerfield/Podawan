package com.dangerfield.features.feed.internal

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Dimension.Pixels
import coil.size.Size
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.podawan.features.feed.internal.R
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.ClickableText
import com.dangerfield.ui.components.text.Text
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

            var urlIndexToLoad by remember { mutableIntStateOf(0) }
            val urlToLoad by remember {
                derivedStateOf { episode.imageUrls.getOrNull(urlIndexToLoad) }
            }

            val imageHeight = 50.dp
            val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }

            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .diskCacheKey(urlToLoad)
                    .memoryCacheKey(urlToLoad)
                    .data(urlToLoad)
                    .size(Size(Pixels(imageHeightPx.toInt()), Pixels(imageHeightPx.toInt())))
                    .build(),
                placeholder = debugPlaceholder(debugPreview = R.drawable.ic_android),
                error = debugPlaceholder(debugPreview = R.drawable.ic_android),
                fallback = debugPlaceholder(debugPreview = R.drawable.ic_android),
                onLoading = null,
                onSuccess = { },
                onError = {
                    if (urlIndexToLoad < episode.imageUrls.lastIndex) {
                        urlIndexToLoad += 1
                    }
                },
                contentScale = ContentScale.FillWidth,
                filterQuality = DrawScope.DefaultFilterQuality,
            )

            Row {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .aspectRatio(1f)
                        .clip(Radii.Card.shape)
                        .border(
                            2.dp,
                            PodawanTheme.colors.border.color,
                            Radii.Card.shape
                        ),
                    painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
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
                imageUrls = listOf(),
                description = loremIpsum(50..100),
                isPlaying = false,
                isDownloaded = false,
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
                imageUrls = listOf(),
                description = loremIpsum(50..100),
                isPlaying = false,
                isDownloaded = false,
                id = "",
                author = "Author Name"
            ),
        )
    }
}


