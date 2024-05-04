package com.dangerfield.libraries.podcast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.dangerfield.libraries.ui.color.rememberDominantColorState
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.previewableImage
import com.kmpalette.DominantColorState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun EpisodeImage(
    imageUrls: ImmutableList<String>,
    modifier: Modifier = Modifier,
    onDominantColorDetected: ((DominantColorState<ImageBitmap>) -> Unit)? = null,
) {
    val dominantColor = rememberDominantColorState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(dominantColor) {
        onDominantColorDetected?.let {
            it(dominantColor)
        }
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .diskCacheKey(imageUrls.firstOrNull())
            .memoryCacheKey(imageUrls.firstOrNull())
            .data(imageUrls.firstOrNull())
            .size(Size.ORIGINAL)
            .build(),
        placeholder = previewableImage(),
        error = previewableImage(),
        fallback = previewableImage(),
        onLoading = null,
        onSuccess = {
            if (onDominantColorDetected != null) {
                coroutineScope.launch {
                    val bitmap = it.result.drawable.toBitmap().asImageBitmap()
                    dominantColor.updateFrom(bitmap)
                }
            }
        },
        contentScale = ContentScale.FillWidth,
        filterQuality = DrawScope.DefaultFilterQuality,
    )

    Image(
        modifier = modifier.aspectRatio(1f),
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.FillWidth,
    )
}

@Preview
@Composable
private fun PreviewEpisodeImage() {
    Preview {
        EpisodeImage(
            imageUrls = persistentListOf("https://via.placeholder.com/150"),
            onDominantColorDetected = {}
        )
    }
}
