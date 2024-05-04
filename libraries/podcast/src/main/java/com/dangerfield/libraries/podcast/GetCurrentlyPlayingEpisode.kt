package com.dangerfield.libraries.podcast

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import podawan.core.Catching

/**
 * Use case to get the displayable episodes from a podcast show
 */
fun interface GetDisplayableEpisodes {
    operator fun invoke(show: PodcastShow): Flow<ImmutableList<DisplayableEpisode>>
}

/**
 * use case to get the displayable version of a specific episode
 */
fun interface GetDisplayableEpisode {
    operator fun invoke(id: String): Flow<Catching<DisplayableEpisode>>
}

/**
 * use case to get the currently playing displayable epside
 */
fun interface GetCurrentlyPlayingEpisode {
    operator fun invoke(): Flow<CurrentlyPlayingEpisode?>
}