package com.dangerfield.features.feed.internal

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.dangerfield.libraries.navigation.ContentSafeAreaBottomPadding
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.VerticalSpacerD300
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.components.HorizontalDivider
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.icon.CircleIcon
import com.dangerfield.libraries.ui.components.icon.IconSize
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.loremIpsum
import com.dangerfield.libraries.ui.scrollbar
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.podawan.features.feed.internal.R
import podawan.core.doNothing

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    title: String,
    episodes: List<DisplayableEpisode>,
    onEpisodePlayClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodePauseClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodeDownloadClicked: (DisplayableEpisode) -> Unit = {}
) {
    val scrollState = rememberLazyListState()

    Screen(modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = Dimension.D1000)
        ) {

            LazyColumn(
                state = scrollState
            ) {

                item {

                    VerticalSpacerD1000()

                    Text(
                        text = title,
                        typography = PodawanTheme.typography.Heading.H1000
                    )

                    VerticalSpacerD1000()
                }

                items(episodes) { episode ->
                    Column {
                        EpisodeItem(
                            episode = episode,
                            onPauseClicked = { onEpisodePauseClicked(episode) },
                            onPlayClicked = { onEpisodePlayClicked(episode) },
                            onDownloadClicked = { onEpisodeDownloadClicked(episode) },
                            onShareClicked = { doNothing() },
                        )

                        HorizontalDivider(color = PodawanTheme.colors.borderDisabled.color)

                        VerticalSpacerD1000()
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(ContentSafeAreaBottomPadding))
                }
            }
        }
    }
}

@Composable
fun debugPlaceholder(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        null
    }

@Composable
@Preview
private fun PreviewScreen(
) {

    fun getRandomEpisode() =
        DisplayableEpisode(
            title = loremIpsum(2),
            releaseDate = "December 12, 2021",
            imageUrl = loremIpsum(2),
            description = loremIpsum(10..20),
            fallbackImageUrl = "",
            isPlaying = true,
            isDownloaded = false,
            id = ""
        )

    Preview {
        FeedScreen(
            title = loremIpsum(2),
            episodes = listOf(
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode()
            ),
        )
    }
}