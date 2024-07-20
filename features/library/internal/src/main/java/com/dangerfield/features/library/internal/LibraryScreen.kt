package com.dangerfield.features.library.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistItem
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.verticalScrollWithBar
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.text.Text
import podawan.core.App

@Composable
fun LibraryScreen(
    playlists: List<Playlist>,
    onNewPlaylistClick: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Screen(
        modifier = modifier,
        topBar = {
            Text(
                modifier = Modifier.padding(Dimension.D800),
                text = "Your Library",
                textAlign = TextAlign.Center,
                typography = PodawanTheme.typography.Heading.H800
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimension.D800)
        ) {

            NewPlaylistItem(
                onClick = onNewPlaylistClick
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fadingEdge(listState)
                    .verticalScrollWithBar(listState)
            ) {
                items(playlists) {
                    PlaylistItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .bounceClick { onPlaylistClick(it) },
                        playlist = it
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewLibraryScreen() {
    Preview {
        LibraryScreen(
            playlists = listOf(
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 34,
                    episodeIds = emptyList(),
                ),
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 123,
                    episodeIds = emptyList(),
                ),
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 34,
                    episodeIds = emptyList(),
                ),
            ),
            onNewPlaylistClick = { -> },
            onPlaylistClick = { c -> },
        )
    }
}

@Preview
@Composable
private fun PreviewLibraryScreenFragmented() {
    Preview(app = App.Fragmented) {
        LibraryScreen(
            playlists = listOf(
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 34,
                    episodeIds = emptyList(),
                ),
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 123,
                    episodeIds = emptyList(),
                ),
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 34,
                    episodeIds = emptyList(),
                ),
            ),
            onNewPlaylistClick = { -> },
            onPlaylistClick = { c -> },
        )
    }
}