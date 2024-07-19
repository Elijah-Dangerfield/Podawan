package com.dangerfield.features.playback.internal.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.podcast.CurrentlyPlaying
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodeImage
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.LocalContentColor
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1400
import com.dangerfield.libraries.ui.VerticalSpacerD1900
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.VerticalSpacerD800
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.elevation
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.button.ButtonStyle
import com.dangerfield.ui.components.dialog.bottomsheet.BasicBottomSheet
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetState
import com.dangerfield.ui.components.dialog.bottomsheet.BottomSheetValue
import com.dangerfield.ui.components.dialog.bottomsheet.rememberBottomSheetState
import com.dangerfield.ui.components.icon.CircleIcon
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.persistentListOf

@Composable
fun EpisodeOptionsBottomSheet(
    episode: DisplayableEpisode,
    bottomSheetState: BottomSheetState,
    onAddToPlaylist: () -> Unit,
    onClose: () -> Unit,
) {

    val isDark = PodawanTheme.colors.background.color.luminance() < 0.5f

    BasicBottomSheet(
        onDismissRequest = onClose,
        showDragHandle = false,
        backgroundColor = PodawanTheme.colors.background.withAlpha(if(isDark) 0.75f else 0.9f),
        state = bottomSheetState,
        content = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .fadingEdge(scrollState)
                    .padding(horizontal = Dimension.D500),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                VerticalSpacerD1900()
                VerticalSpacerD1900()

                EpisodeImage(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .elevation(Elevation.AppBar, shape = Radii.Card.shape),
                    imageUrls = episode.imageUrls,
                )

                VerticalSpacerD800()

                EpisodeInfo(episode)

                VerticalSpacerD1400()

                Option(
                    icon = PodawanIcon.Add(""),
                    text = "Add to playlist",
                    onClick = onAddToPlaylist
                )

                Option(
                    icon = PodawanIcon.Queue(""),
                    text = "Add to queue",
                    onClick = { }
                )

                Option(
                    icon = PodawanIcon.Share(""),
                    text = "Share",
                    onClick = {}
                )

                Option(
                    icon = PodawanIcon.ArrowCircleDown(""),
                    text = "Download",
                    onClick = {}
                )

            }

        },
        stickyBottomContent = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.NoBackground,
                onClick = onClose,
            ) {
                Text(
                    text = "Close",
                    typography = PodawanTheme.typography.Label.L600.Bold,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                )
            }
        }
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun EpisodeInfo(episode: DisplayableEpisode) {

    val title by remember {
        derivedStateOf { episode.title }
    }
    val author by remember {
        derivedStateOf { episode.author }
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .basicMarquee(),
        text = title,
        typography = PodawanTheme.typography.Heading.H700,
        maxLines = 1,
        textAlign = TextAlign.Center
    )

    VerticalSpacerD500()

    author?.let { nonNullAuthor ->
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .basicMarquee(),
            text = nonNullAuthor,
            typography = PodawanTheme.typography.Label.L600,
            maxLines = 1,
            colorResource = LocalContentColor.current.withAlpha(0.6f),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun Option(
    icon: PodawanIcon,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimension.D1000)
            .bounceClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIcon(
            icon = icon,
            iconSize = IconSize.Small,
            padding = Dimension.D100
        )

        HorizontalSpacerD500()

        Text(
            text = text,
            typography = PodawanTheme.typography.Label.L600.Bold,
        )
    }
}

@Preview
@Composable
private fun PreviewEpisodeOptionsBottomSheet() {
    Preview {
        EpisodeOptionsBottomSheet(
            DisplayableEpisode(
                id = "1",
                title = "Healing",
                releaseDate = "2021-08-01",
                imageUrls = persistentListOf(),
                description = "Emma Hinkley",
                author = "Emma Hinkley",
                isDownloaded = false,
                playback = EpisodePlayback.None(),
            ),
            onClose = {},
            onAddToPlaylist = {},
            bottomSheetState = rememberBottomSheetState(initialState = BottomSheetValue.Expanded),
        )

    }
}