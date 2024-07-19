package com.dangerfield.features.playlist.internal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dangerfield.features.playlist.PlaylistImage
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.FieldState
import com.dangerfield.libraries.ui.HorizontalSpacerD200
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.fadingEdge
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.preview.previewableImage
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.verticalScrollWithBar
import com.dangerfield.ui.components.HorizontalDivider
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import com.dangerfield.ui.components.text.UnderlineInputField
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import java.io.File

@Composable
fun EditPlaylistScreen(
    heroImageUrl: String?,
    nameFieldState: FieldState<String>,
    descriptionFieldState: FieldState<String>,
    areChangesValid: Boolean,
    modifier: Modifier = Modifier,
    episodes: ImmutableList<DisplayableEpisode>,
    hasMadeChanges: Boolean = false,
    onClose: () -> Unit = {},
    onSave: () -> Unit = {},
    onUpdateName: (String) -> Unit = {},
    onUpdateDescription: (String) -> Unit = {},
    updatePhoto: (Uri) -> Unit = {}
) {

    val scrollState = rememberLazyListState()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let(updatePhoto) }
    )

    Screen(
        modifier,
        topBar = {
            Row {
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.padding(Dimension.D500),
                    icon = PodawanIcon.Close(""),
                    onClick = onClose
                )
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                            heroImageUrl = heroImageUrl,
                            onEditPhotoClicked = {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ImageOnly
                                    )
                                )
                            }
                        )

                        VerticalSpacerD1200()

                        UnderlineInputField(
                            fieldState = nameFieldState,
                            onFieldUpdated = onUpdateName,
                            hint = "Enter a name"
                        )

                        VerticalSpacerD500()


                        UnderlineInputField(
                            fieldState = descriptionFieldState,
                            onFieldUpdated = onUpdateDescription,
                            hint = "Add a Description",
                        )

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

                        Column {
                            EditEpisodeItem(
                                modifier = Modifier.fillMaxWidth(),
                                episode = episode,
                            )

                            VerticalSpacerD1000()

                            if (episodes.last() != episode) {
                                HorizontalDivider(color = PodawanTheme.colors.borderDisabled.color)
                                VerticalSpacerD1000()
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = hasMadeChanges,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimension.D500),
                        enabled = areChangesValid,
                        onClick = onSave
                    ) {
                        Text(text = "Save")
                    }

                    VerticalSpacerD1000()
                }
            }
        }
    }
}


@Composable
private fun PlaylistImage(
    heroImageUrl: String?,
    onEditPhotoClicked: () -> Unit
) {
    Box {
        PlaylistImage(
            modifier = Modifier.fillMaxWidth(0.45f),
            heroImageUrl
        )

        Row(
            modifier = Modifier
                .bounceClick { onEditPhotoClicked() }
                .align(Alignment.BottomEnd)
                .graphicsLayer {
                    translationY = (this.size.height / 3)
                    translationX = (this.size.width / 5)
                }
                .background(
                    color = PodawanTheme.colors.surfaceSecondary.color,
                    shape = Radii.Round.shape
                )
                .padding(Dimension.D300)

        ) {
            HorizontalSpacerD200()
            Icon(podawanIcon = PodawanIcon.Pencil(""), iconSize = IconSize.Smallest)
            HorizontalSpacerD200()
            Text(
                text = "Edit",
                typography = PodawanTheme.typography.Label.L500,
            )
            HorizontalSpacerD200()
        }
    }
}

@Preview
@Composable
private fun PreviewEditPlaylistScreen() {
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

        EditPlaylistScreen(
            nameFieldState = FieldState.Idle("Playlist Name"),
            descriptionFieldState = FieldState.Idle(loremIpsum(10)),
            heroImageUrl = null,
            modifier = Modifier,
            episodes = episodes.toImmutableList(),
            hasMadeChanges = true,
            onClose = {},
            onSave = {},
            onUpdateName = {},
            onUpdateDescription = {},
            areChangesValid = true
        )
    }
}