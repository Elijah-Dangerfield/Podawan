package com.dangerfield.features.playlist.internal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.dangerfield.features.playlist.PlaylistImage
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodeItem
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.makeLookClickable
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.preview.previewableImage
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.verticalScrollWithBar
import com.dangerfield.libraries.ui.visibility
import com.dangerfield.ui.components.HorizontalDivider
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.button.ButtonStyle
import com.dangerfield.ui.components.icon.CircleIcon
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import podawan.core.doNothing
import java.io.File

@Composable
fun PlaylistScreen(
    name: String,
    description: String?,
    heroImageUrl: String?,
    modifier: Modifier = Modifier,
    currentlyPlayingEpisode: DisplayableEpisode? = null,
    episodes: ImmutableList<DisplayableEpisode>,
    onEpisodePlayClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodePauseClicked: (DisplayableEpisode) -> Unit = {},
    onEpisodeDownloadClicked: (DisplayableEpisode) -> Unit = {},
    onCurrentlyPlayingEnterView: () -> Unit = {},
    onCurrentlyPlayingExitView: () -> Unit = {},
    onClickEpisode: (DisplayableEpisode) -> Unit = {},
    onAddToPlaylistClicked: (DisplayableEpisode) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onEditClicked: () -> Unit = {}
) {

    val scrollState = rememberLazyListState()

    val isCurrentlyPlayingVisible by remember(currentlyPlayingEpisode, scrollState) {
        derivedStateOf {
            scrollState.layoutInfo
                .visibleItemsInfo
                .any { it.key == currentlyPlayingEpisode?.id }
        }
    }

    LaunchedEffect(isCurrentlyPlayingVisible) {
        if (isCurrentlyPlayingVisible) {
            onCurrentlyPlayingEnterView()
        } else {
            onCurrentlyPlayingExitView()
        }
    }

    Screen(
        modifier,
        topBar = {
            Row {
                IconButton(
                    modifier = Modifier.padding(Dimension.D500),
                    icon = PodawanIcon.ArrowBack(""),
                    onClick = onNavigateBack
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.padding(Dimension.D500),
                    icon = PodawanIcon.Pencil(""),
                    onClick = onEditClicked
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimension.D500),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fadingEdge(scrollState)
                    .verticalScrollWithBar(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {

                    VerticalSpacerD1200()

                    PlaylistImage(
                        modifier = Modifier.fillMaxWidth(0.45f),
                        heroImageUrl
                    )

                    VerticalSpacerD1200()

                    Text(text = name)

                    VerticalSpacerD500()

                    if (description != null) {
                        Text(
                            text = description,
                            typography = PodawanTheme.typography.Body.B600.Thin,
                            maxLines = 2
                        )
                        VerticalSpacerD500()

                    }

                    VerticalSpacerD1200()
                    if (episodes.isEmpty()) {
                        Text(
                            text = "Looks like you havent added any episodes or clips yet. Get exploring!",
                            typography = PodawanTheme.typography.Body.B600.Italic,
                            colorResource = PodawanTheme.colors.textSecondary
                        )
                    }
                }

                items(episodes) { episode ->
                    EpisodeItem(
                        episode = if (episode.id == currentlyPlayingEpisode?.id) {
                            currentlyPlayingEpisode // so that live updates are shown
                        } else {
                            episode
                        },
                        onPauseClicked = { onEpisodePauseClicked(episode) },
                        onPlayClicked = { onEpisodePlayClicked(episode) },
                        onDownloadClicked = { onEpisodeDownloadClicked(episode) },
                        onShareClicked = { doNothing() },
                        onClickEpisode = { onClickEpisode(episode) },
                        onAddToPlaylistClicked = { onAddToPlaylistClicked(episode) }
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


@Preview
@Composable
private fun PreviewPlaylistScreenEmpty() {
    Preview {
        PlaylistScreen(
            name = "Playlist Name",
            description = loremIpsum(12),
            heroImageUrl = null,
            modifier = Modifier,
            currentlyPlayingEpisode = null,
            episodes = persistentListOf(),
            onEpisodePlayClicked = { _ -> },
            onEpisodePauseClicked = { _ -> },
            onEpisodeDownloadClicked = { _ -> },
            onCurrentlyPlayingEnterView = { -> },
            onCurrentlyPlayingExitView = { -> },
            onClickEpisode = { _ -> },
            onAddToPlaylistClicked = { _ -> }
        )
    }
}

@Preview
@Composable
private fun PreviewPlaylistScreenFilled() {
    Preview {

        val episodes = remember {
            listOf(0, 1, 2, 3).map {
                DisplayableEpisode(
                    id = it.toString(),
                    title = "Episode Title $it",
                    description = loremIpsum(10),
                    isDownloaded = false,
                    releaseDate = null,
                    imageUrls = persistentListOf(),
                    author = loremIpsum(3),
                    playback = EpisodePlayback.None(),
                )
            }
        }

        PlaylistScreen(
            name = "Playlist Name",
            description = loremIpsum(12),
            heroImageUrl = null,
            modifier = Modifier,
            currentlyPlayingEpisode = null,
            episodes = episodes.toImmutableList(),
            onEpisodePlayClicked = { _ -> },
            onEpisodePauseClicked = { _ -> },
            onEpisodeDownloadClicked = { _ -> },
            onCurrentlyPlayingEnterView = { -> },
            onCurrentlyPlayingExitView = { -> },
            onClickEpisode = { _ -> },
            onAddToPlaylistClicked = { _ -> }
        )
    }
}