package com.dangerfield.features.playlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.VerticalSpacerD100
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.text.Text

@Composable
fun PlaylistItem(
    playlist: Playlist,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = Dimension.D500),
        verticalAlignment = Alignment.CenterVertically
    ) {

        PlaylistImage(
            modifier = Modifier.width(50.dp),
            heroImageUrl = playlist.imageUrl
        )
        HorizontalSpacerD500()

        Column {
            if (playlist.name.isNotEmpty()) {
                Text(
                    maxLines = 2,
                    text = playlist.name,
                    typography = PodawanTheme.typography.Body.B600.Bold
                )
                VerticalSpacerD100()
            }

            VerticalSpacerD100()

            if (playlist.description.isNotEmpty()) {
                Text(
                    maxLines = 1,
                    text = playlist.description,
                    typography = PodawanTheme.typography.Body.B600,
                    colorResource = PodawanTheme.colors.textSecondary
                )
            }
        }
    }
}

@Composable
@Preview
private fun PreviewPlaylistItem() {
    Preview {
        PlaylistItem(
            playlist = Playlist(
                imageUrl = "https://www.example.com/image.jpg",
                name = "Playlist Name",
                description = "Playlist Description",
                id = 1694,
                episodeIds = listOf(),
            )
        )
    }
}