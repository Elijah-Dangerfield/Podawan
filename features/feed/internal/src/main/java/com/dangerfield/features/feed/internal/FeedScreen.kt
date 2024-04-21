package com.dangerfield.features.feed.internal

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.navigation.ContentSafeAreaBottomPadding
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.ui.components.HorizontalDivider
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import podawan.core.App
import podawan.core.doNothing

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    title: String,
    episodes: List<DisplayableEpisode>,
    onEpisodePlayClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodePauseClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodeDownloadClicked: (DisplayableEpisode) -> Unit = {},
    onClickTitle: () -> Unit
) {
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        Log.d("Elijah"," Loading FeedScreen")
    }

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
                        modifier = Modifier.bounceClick {
                            onClickTitle()
                        },
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
private fun PreviewScreen() {

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
        val appName = LocalAppConfiguration.current.appName

        FeedScreen(
            title = appName,
            episodes = listOf(
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
            ),
            onClickTitle = {}
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
            imageUrl = loremIpsum(2),
            description = loremIpsum(10..20),
            fallbackImageUrl = "",
            isPlaying = true,
            isDownloaded = false,
            id = ""
        )

    Preview(
        app = App.StuffYouShouldKnow
    ){
        val appName = LocalAppConfiguration.current.appName

        FeedScreen(
            title = appName,
            episodes = listOf(
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
                getRandomEpisode(),
            ),
            onClickTitle = {}
        )
    }
}