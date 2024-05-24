package com.dangerfield.features.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.VerticalSpacerD100
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.previewableImage
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.text.Text

@Composable
fun PlaylistItem(
    playlist: Playlist,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = Dimension.D500),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PlaylistImage(playlist)

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
}

@Composable
private fun PlaylistImage(playlist: Playlist) {
    if (playlist.imageUrl.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(PodawanTheme.colors.surfaceDisabled.color)
        )
    } else {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .diskCacheKey(playlist.imageUrl)
                .memoryCacheKey(playlist.imageUrl)
                .data(playlist.imageUrl)
                .size(Size.ORIGINAL)
                .build(),
            placeholder = previewableImage(),
            error = previewableImage(),
            fallback = previewableImage(),
            onLoading = null,
            onSuccess = {},
            contentScale = ContentScale.FillWidth,
            filterQuality = DrawScope.DefaultFilterQuality,
        )

        Image(
            modifier = Modifier
                .width(50.dp)
                .aspectRatio(1f),
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
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
                episodes = listOf(),
            )
        )
    }
}