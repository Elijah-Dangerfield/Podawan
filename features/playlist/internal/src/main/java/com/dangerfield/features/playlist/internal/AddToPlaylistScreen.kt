package com.dangerfield.features.playlist.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistItem
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Selectable
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.VerticalSpacerD1600
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.checkbox.CircleCheckbox
import com.dangerfield.ui.components.text.Text

@Composable
fun AddToPlaylistScreen(
    playlists: List<Selectable<Playlist>>,
    onPlaylistChecked: (Selectable<Playlist>) -> Unit,
    onSave: () -> Unit,
    haveChangesBeenMade: Boolean,
    modifier: Modifier = Modifier
) {

    Screen(
        modifier = modifier
            .padding(top = Dimension.D1000)
            .padding(horizontal = Dimension.D500)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.padding(it)) {
                Text(text = "Add to Playlist")

                VerticalSpacerD1000()

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = it
                ) {
                    items(playlists) { playlist ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .bounceClick {
                                    onPlaylistChecked(playlist.toggled())
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            PlaylistItem(
                                modifier = Modifier,
                                playlist = playlist.item,
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            CircleCheckbox(
                                modifier = Modifier.size(Dimension.D1000),
                                checked = playlist.isSelected,
                                onCheckedChange = { checked ->
                                    onPlaylistChecked(playlist.toggled())
                                }
                            )

                            HorizontalSpacerD200()
                        }

                    }

                    item {
                        VerticalSpacerD1600()
                    }
                }

            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = haveChangesBeenMade,
                    onClick = onSave
                ) {
                    Text(text = "Save")
                }

                VerticalSpacerD1000()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddToPlaylistScreen() {
    Preview {
        AddToPlaylistScreen(
            playlists = listOf(
                Selectable(
                    Playlist(
                        id = 1085,
                        name = loremIpsum(2),
                        description = loremIpsum(4),
                        imageUrl = null,
                        episodeIds = listOf()
                    ),
                    isSelected = true
                ),

                Selectable(
                    Playlist(
                        id = 1085,
                        name = loremIpsum(2),
                        description = loremIpsum(4),
                        imageUrl = null,
                        episodeIds = listOf()
                    ),
                    isSelected = false
                ),

                Selectable(
                    Playlist(
                        id = 1085,
                        name = loremIpsum(20),
                        description = loremIpsum(4),
                        imageUrl = null,
                        episodeIds = listOf()
                    ),
                    isSelected = false
                ),

                Selectable(
                    Playlist(
                        id = 1085,
                        name = loremIpsum(2),
                        description = loremIpsum(4),
                        imageUrl = null,
                        episodeIds = listOf()
                    ),
                    isSelected = true
                ),
            ),
            onPlaylistChecked = {},
            onSave = { -> },
            haveChangesBeenMade = true,

            )
    }
}