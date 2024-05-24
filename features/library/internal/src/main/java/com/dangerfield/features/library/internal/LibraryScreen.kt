package com.dangerfield.features.library.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistItem
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.verticalScrollWithBar
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.text.Text

@Composable
fun LibraryScreen(
    playlists: List<Playlist>,
    onNewPlaylistClick: () -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Screen(modifier = modifier) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Dimension.D800)
                .padding(top = Dimension.D800)
        ) {
            Text(text = "Library")

            VerticalSpacerD500()

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
                        modifier = Modifier.bounceClick { onPlaylistClick(it) },
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
                    episodes = emptyList(),
                ),
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 123,
                    episodes = emptyList(),
                ),
                com.dangerfield.features.playlist.Playlist(
                    imageUrl = "https://via.placeholder.com/150",
                    name = "Playlist 2",
                    description = "Description 2",
                    id = 34,
                    episodes = emptyList(),
                ),
            ),
            onNewPlaylistClick = { -> },
            onPlaylistClick = { c -> },
        )
    }
}