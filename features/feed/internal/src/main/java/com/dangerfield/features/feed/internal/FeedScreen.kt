package com.dangerfield.features.feed.internal

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
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
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD200
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.preview.previewableImage
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.verticalScrollWithBar
import com.dangerfield.ui.components.HorizontalDivider
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import podawan.core.App
import podawan.core.doNothing

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    showTitle: String,
    showDescription: String,
    heroImageUrl: String?,
    episodes: ImmutableList<DisplayableEpisode>,
    onEpisodePlayClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodePauseClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodeDownloadClicked: (DisplayableEpisode) -> Unit = {},
    onClickTitle: () -> Unit,
    onClickEpisode: (DisplayableEpisode) -> Unit = {},
) {
    val scrollState = rememberLazyListState()

    Screen(modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .fadingEdge(scrollState)
                .verticalScrollWithBar(scrollState)
                .padding(horizontal = Dimension.D500)
        ) {

            LazyColumn(
                state = scrollState
            ) {

                item {

                    VerticalSpacerD1200()

                    Row {

                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .diskCacheKey(heroImageUrl)
                                .memoryCacheKey(heroImageUrl)
                                .data(heroImageUrl)
                                .size(Size.ORIGINAL)
                                .build(),
                            placeholder = previewableImage(),
                            error = previewableImage(),
                            fallback = previewableImage(),
                            onLoading = null,
                            onSuccess = { },
                            onError = { },
                            contentScale = ContentScale.FillWidth,
                            filterQuality = DrawScope.DefaultFilterQuality,
                        )

                        Image(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
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
                                modifier = Modifier.bounceClick { onClickTitle() },
                                text = showTitle,
                                typography = PodawanTheme.typography.Heading.H1000
                            )

                            VerticalSpacerD200()

                            Text(
                                text = showDescription,
                                typography = PodawanTheme.typography.Body.B500,
                                maxLines = 2
                            )
                        }
                    }

                    VerticalSpacerD1200()
                }

                items(episodes) { episode ->
                    Column {
                        EpisodeItem(
                            episode = episode,
                            onPauseClicked = { onEpisodePauseClicked(episode) },
                            onPlayClicked = { onEpisodePlayClicked(episode) },
                            onDownloadClicked = { onEpisodeDownloadClicked(episode) },
                            onShareClicked = { doNothing() },
                            onClickEpisode = { onClickEpisode(episode) }
                        )

                        if (episodes.last() != episode) {
                            HorizontalDivider(color = PodawanTheme.colors.borderDisabled.color)
                            VerticalSpacerD1000()
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewScreen() {

    fun getRandomEpisode() =
        DisplayableEpisode(
            title = loremIpsum(2),
            releaseDate = "December 12, 2021",
            imageUrls = persistentListOf(),
            description = loremIpsum(10..20),
            isDownloaded = false,
            id = "",
            author = "Author Name",
            isPlaying = false,
            isLoading = false
        )

    Preview {
        val appName = LocalAppConfiguration.current.appName

        FeedScreen(
            showDescription = loremIpsum(20),
            showTitle = appName,
            episodes = persistentListOf(
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
            ),
            onClickTitle = {},
            heroImageUrl = "https://i.scdn.co/image/ab67616d0000b273f3b3b3b3b3b3b3b3b3b3b3b3"
        )
    }
}

@Composable
@Preview
private fun PreviewScreenSYSK() {

    fun getRandomEpisode() =
        DisplayableEpisode(
            title = loremIpsum(2),
            releaseDate = "December 12, 2021",
            imageUrls = persistentListOf(),
            description = loremIpsum(10..20),
            isPlaying = false,
            isLoading = false,
            isDownloaded = false,
            id = "",
            author = "Author Name"
        )

    Preview(
        app = App.StuffYouShouldKnow
    ) {
        val appName = LocalAppConfiguration.current.appName

        FeedScreen(
            showDescription = loremIpsum(20),
            showTitle = appName,
            episodes = persistentListOf(
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
            ),
            onClickTitle = {},
            heroImageUrl = "https://i.scdn.co/image/ab67616d0000b273f3b3b3b3b3b3b3b3b3b3b3b3"
        )
    }
}