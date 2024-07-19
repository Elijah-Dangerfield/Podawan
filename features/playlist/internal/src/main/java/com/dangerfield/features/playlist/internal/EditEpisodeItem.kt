package com.dangerfield.features.playlist.internal
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodeImage
import com.dangerfield.libraries.podcast.EpisodePlayback.Playing
import com.dangerfield.libraries.ui.Dimension.D1000
import com.dangerfield.libraries.ui.Dimension.D1200
import com.dangerfield.libraries.ui.Dimension.D1500
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.VerticalSpacerD500
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.preview.loremIpsum
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.LinearProgressIndicator
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.persistentListOf
import podawan.core.allOrNone

@Composable
fun EditEpisodeItem(
    episode: DisplayableEpisode,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EpisodeImage(
                modifier = Modifier
                    .width(D1200)
                    .clip(Radii.Card.shape)
                    .border(
                        2.dp,
                        PodawanTheme.colors.border.color,
                        Radii.Card.shape
                    ),
                imageUrls = episode.imageUrls,
            )

            HorizontalSpacerD500()

            Text(
                text = episode.title,
                maxLines = 2,
                typography = PodawanTheme.typography.Heading.H500,
            )
        }
    }
}
@Composable
@Preview
private fun PreviewEpisodeItem() {
    Preview {
        EditEpisodeItem(
            episode = DisplayableEpisode(
                title = loremIpsum(3),
                releaseDate = "Dec 12, 2021",
                imageUrls = persistentListOf(),
                description = loremIpsum(20..30),
                isDownloaded = false,
                id = "",
                author = "Author Name",
                playback = Playing()
            ),
        )
    }
}

@Composable
@Preview
private fun PreviewEpisodeItemLonger() {
    Preview {
        EditEpisodeItem(
            episode = DisplayableEpisode(
                title = loremIpsum(12),
                releaseDate = "Dec 12, 2021",
                imageUrls = persistentListOf(),
                description = loremIpsum(20..30),
                isDownloaded = false,
                id = "",
                author = "Author Name",
                playback = Playing()
            ),
        )
    }
}