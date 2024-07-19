package com.dangerfield.features.feed.internal

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD300
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.chipBackground
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.previewableImage
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.NonLazyVerticalGrid
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text

@Composable
fun ShowDetailsScreen(
    modifier: Modifier = Modifier,
    showTitle: String,
    showDescription: String,
    episodeCount: Int,
    categories: List<String>,
    heroImageUrl: String?,
    onNavigateBack: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Screen(
        modifier,
        topBar = {
            Row {
                IconButton(
                    modifier = Modifier.padding(Dimension.D500),
                    icon = PodawanIcon.ArrowBack(""),
                    onClick = onNavigateBack
                )
            }
        },
    ) {
        Column(
            modifier =
            Modifier
                .padding(it)
                .padding(horizontal = Dimension.D800)
                .verticalScroll(scrollState)
                .fadingEdge(scrollState),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

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
                    .fillMaxWidth(0.7f)
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

            VerticalSpacerD500()

            Text(
                text = showTitle,
                typography = PodawanTheme.typography.Heading.H1000
            )

            
            VerticalSpacerD500()
            
            NonLazyVerticalGrid(columns = 3, data = categories) { index, item ->
                Text(
                    modifier = Modifier.chipBackground(
                        PodawanTheme.colors.surfaceSecondary.color,
                        padding = Dimension.D500
                    ),
                    text = item,
                    typography = PodawanTheme.typography.Body.B600
                )
            }

            VerticalSpacerD1200()

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Episodes: $episodeCount",
                typography = PodawanTheme.typography.Body.B600
            )

            VerticalSpacerD300()

            Text(
                text = showDescription,
                typography = PodawanTheme.typography.Body.B500,
            )
        }
    }
}

@Suppress("MaxLineLength")
@Preview
@Composable
private fun ShowDetailsScreenPreview() {
    Preview {
        ShowDetailsScreen(
            showTitle = "The Mandalorian",
            showDescription = "The Mandalorian is a space western television series created by Jon Favreau for the streaming service Disney+. It is the first live-action series in the Star Wars franchise, beginning five years after the events of Return of the Jedi (1983).",
            heroImageUrl = "https://static.wikia.nocookie.net/starwars/images/4/4e/The_Mandalorian_Season_2_Poster.jpeg/revision/latest/scale-to-width-down/1000?cb=20201030181957",
            episodeCount = 23,
            categories = listOf("Action", "Adventure", "Fantasy")
        )
    }
}
