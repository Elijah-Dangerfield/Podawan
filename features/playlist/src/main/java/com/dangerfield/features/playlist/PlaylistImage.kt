package com.dangerfield.features.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.previewableImage
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import java.io.File

@Composable
fun PlaylistImage(
    modifier: Modifier = Modifier,
    heroImageUrl: String?
) {

    if (heroImageUrl == null) {
        Column(
            modifier = modifier
                .defaultMinSize(minHeight = 24.dp)
                .aspectRatio(1f)
                .clip(Radii.Card.shape)
                .border(
                    2.dp,
                    PodawanTheme.colors.border.color,
                    Radii.Card.shape
                )
                .background(PodawanTheme.colors.surfaceDisabled.color),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                podawanIcon = PodawanIcon.LibraryOutline(null),
                iconSize = IconSize.Large,
            )
        }
    } else {
        val painter = rememberAsyncImagePainter(
            model = File(heroImageUrl),
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
            modifier = modifier
                .defaultMinSize(minHeight = 24.dp)
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
    }
}

@Preview
@Composable
private fun PreviewPlaylistNoImage() {
    Preview {
        PlaylistImage(
            heroImageUrl = null
        )
    }
}

@Preview
@Composable
private fun PreviewPlaylistNoImageSmall() {
    Preview {
        PlaylistImage(
            modifier = Modifier.width(50.dp),
            heroImageUrl = null
        )
    }
}

@Preview
@Composable
private fun PreviewPlaylistWithImage() {
    Preview {
        PlaylistImage(
            heroImageUrl = "android.resource://com.dangerfield.libraries.ui/drawable/ic_podawan_logo"
        )
    }
}