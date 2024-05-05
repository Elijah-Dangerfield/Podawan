package com.dangerfield.libraries.podcast.internal

import com.dangerfield.features.playback.PlayerStateRepository
import com.dangerfield.libraries.podcast.DisplayableEpisode
import com.dangerfield.libraries.podcast.EpisodePlayback.None
import com.dangerfield.libraries.podcast.GetDisplayableEpisodes
import com.dangerfield.libraries.podcast.PodcastShow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import se.ansman.dagger.auto.AutoBind
import timber.log.Timber
import java.util.Timer
import javax.inject.Inject

@AutoBind
class GetDisplayableEpisodesImpl @Inject constructor(
    private val playerStateRepository: PlayerStateRepository
) : GetDisplayableEpisodes {

    /**
     * Takes a podcast shows episodes, converts them to displayable episodes, & keeps those displayable
     * episodes up to date with the current playback state
     */
    // TODO investiage: do I need to map through every single episode when only one episode updates
    override fun invoke(show: PodcastShow): Flow<ImmutableList<DisplayableEpisode>> {
        val runningMap = show
            .episodes.map { it.toDisplayable(show = show, isPlaying = false, isLoading = false) }
            .associateBy { it.id }
            .toMutableMap()

        var lastPlayingId: String? = null

        return playerStateRepository
            .getPlayerStateFlow()
            .map { getCurrentlyPlayingDetails(it) }
            .map { (currentlyPlayingId, currentlyPlayingStatus) ->
                Timber.i("playback update: currentlyPlayingId: $currentlyPlayingId, currentlyPlayingStatus: $currentlyPlayingStatus")
                if (currentlyPlayingId != null && currentlyPlayingStatus != null) {
                    // update the last playing episode
                    lastPlayingId?.let { lastPlayingId ->
                        runningMap[lastPlayingId]?.let {
                            runningMap[lastPlayingId] =
                                it.copy(isPlaying = false, isLoading = false)
                        }
                    }

                    // update new playing
                    runningMap[currentlyPlayingId]?.let {
                        runningMap[currentlyPlayingId] =
                            it.copy(
                                isPlaying = currentlyPlayingStatus.isPlaying,
                                isLoading = currentlyPlayingStatus.isLoading
                            )
                    }

                    lastPlayingId = currentlyPlayingId

                }

                runningMap.values.toPersistentList()
            }
    }
}



