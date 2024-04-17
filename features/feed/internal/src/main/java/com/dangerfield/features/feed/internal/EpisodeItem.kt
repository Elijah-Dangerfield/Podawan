package com.dangerfield.features.feed.internal

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD300
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.components.icon.IconButton
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.podawan.features.feed.internal.R

@Composable
fun EpisodeItem(
    episode: DisplayableEpisode,
    onPauseClicked: () -> Unit = {},
    onPlayClicked: () -> Unit = {},
    onDownloadClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
) {
    var urlToLoad by remember { mutableStateOf(episode.imageUrl) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .diskCacheKey(urlToLoad)
            .memoryCacheKey(urlToLoad)
            .data(urlToLoad)
            .size(Size.ORIGINAL)
            .build(),
        placeholder = debugPlaceholder(debugPreview = R.drawable.ic_android),
        error = debugPlaceholder(debugPreview = R.drawable.ic_android),
        fallback = debugPlaceholder(debugPreview = R.drawable.ic_android),
        onLoading = null,
        onSuccess = { },
        onError = { urlToLoad = episode.fallbackImageUrl },
        contentScale = ContentScale.FillWidth,
        filterQuality = DrawScope.DefaultFilterQuality,
    )

    Column {
        Row {
            Image(
                modifier = Modifier
                    .width(60.dp)
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

        VerticalSpacerD300()

        Text(
            text = episode.releaseDate,
            typography = PodawanTheme.typography.Body.B400,
        )

        VerticalSpacerD500()

        val playingIcon = if (episode.isPlaying) PodawanIcon.Pause("") else PodawanIcon.Play("")
        val downloadIcon = if (episode.isDownloaded) PodawanIcon.Check("") else PodawanIcon.ArrowCircleDown("")
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

        VerticalSpacerD500()
    }
}


@Composable
@Preview
private fun PreviewEpisodeItem() {
    Preview {
        EpisodeItem(
            episode = DisplayableEpisode(
                title = loremIpsum(3..10),
                releaseDate = "Dec 12, 2021",
                imageUrl = "",
                description = loremIpsum(20..30),
                fallbackImageUrl = "",
                isPlaying = false,
                isDownloaded = false,
                id = ""
            ),
        )
    }
}

@Composable
@Preview
private fun PreviewEpisodeItemPlaying() {
    Preview {
        EpisodeItem(
            episode = DisplayableEpisode(
                title = loremIpsum(3..10),
                releaseDate = "Dec 12, 2021",
                imageUrl = "",
                description = loremIpsum(20..30),
                fallbackImageUrl = "",
                isPlaying = true,
                isDownloaded = true,
                id = ""
            ),
        )
    }
}