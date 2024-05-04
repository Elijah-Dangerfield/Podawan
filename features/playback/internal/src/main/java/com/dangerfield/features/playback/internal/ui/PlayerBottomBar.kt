package com.dangerfield.features.playback.internal.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.podcast.CurrentlyPlayingEpisode
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback.Paused
import com.dangerfield.libraries.podcast.EpisodePlayback.Playing
import com.dangerfield.libraries.podcast.EpisodeImage
import com.dangerfield.libraries.podcast.EpisodePlayback
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.HorizontalSpacerD600
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.elevation
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.CircularProgressIndicator
import com.dangerfield.ui.components.ProgressRow
import com.dangerfield.ui.components.icon.IconButton
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerBottomBar(
    episode: CurrentlyPlayingEpisode,
    onClick: () -> Unit,
    onPauseClicked: () -> Unit,
    onPlayClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .bounceClick(onClick = onClick)
            .fillMaxWidth()
            .elevation(Elevation.Button, shape = Radii.Card.shape)
            .clip(Radii.Card.shape)
            .background(PodawanTheme.colors.surfaceSecondary.color)
            .height(IntrinsicSize.Min),
    ) {
        Row(
            modifier = Modifier.padding(Dimension.D600),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            EpisodeImage(
                modifier = Modifier.height(Dimension.D1200),
                imageUrls = episode.episode.imageUrls,
            )

            HorizontalSpacerD600()

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = episode.episode.title,
                    typography = PodawanTheme.typography.Label.L600.SemiBold,
                    maxLines = 1
                )

                episode.episode.author?.let {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = it,
                        typography = PodawanTheme.typography.Label.L600,
                        maxLines = 1,
                        colorResource = PodawanTheme.colors.textSecondary
                    )
                }
            }

            HorizontalSpacerD600()

            val playingIcon = if (episode.episodePlayback.isPlaying) {
                PodawanIcon.Pause(null)
            } else {
                PodawanIcon.Play(null)
            }

            if (episode.episodePlayback.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dimension.D800),
                    strokeWidth = Dimension.D50,
                    color = PodawanTheme.colors.onBackground.color,
                )
            } else {
                IconButton(
                    icon = playingIcon,
                    onClick = {
                        if (episode.episodePlayback.isPlaying) {
                            onPauseClicked()
                        } else {
                            onPlayClicked()
                        }
                    },
                    backgroundColor = PodawanTheme.colors.onBackground,
                    iconColor = PodawanTheme.colors.background,
                    size = IconButton.Size.Small,
                )
            }
        }

        if (
            episode.episodePlayback.duration.inWholeSeconds > 0
            && episode.episodePlayback.progress.inWholeSeconds > 0
        ) {
            ProgressRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimension.D50),
                progress = (episode.episodePlayback.progress.inWholeSeconds.toFloat() / episode.episodePlayback.duration.inWholeSeconds)
            )
        }
    }
}

@Composable
@Preview
private fun PreviewPlayerBottomBar() {
    Preview {
        PlayerBottomBar(
            CurrentlyPlayingEpisode(
                DisplayableEpisode(
                    id = "1",
                    title = "Healing",
                    releaseDate = "2021-08-01",
                    imageUrls = persistentListOf(),
                    description = "Emma Hinkley",
                    author = "Emma Hinkley",
                    isDownloaded = false,
                    isPlaying = false,
                    isLoading = false
                ),
                episodePlayback = EpisodePlayback.None()
            ),
            onPauseClicked = {},
            onPlayClicked = {},
            onClick = {},
        )
    }
}

@Composable
@Preview
private fun PreviewPlayerBottomBarPaused() {
    Preview {
        PlayerBottomBar(
            CurrentlyPlayingEpisode(
                DisplayableEpisode(
                    id = "1",
                    title = "Healing",
                    releaseDate = "2021-08-01",
                    imageUrls = persistentListOf(),
                    description = "Emma Hinkley",
                    author = "Emma Hinkley",
                    isDownloaded = false,
                    isPlaying = false,
                    isLoading = false
                ),
                episodePlayback = EpisodePlayback.None()
            ),
            onPauseClicked = {},
            onPlayClicked = {},
            onClick = {}
        )
    }
}