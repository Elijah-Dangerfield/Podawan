package com.dangerfield.libraries.podcast

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import podawan.core.Catching

/*
2 classes
DisplayableEpisode
DisplayablePlayingEpisode

Use cases:

GetDisplayableEpisodes List<DisplayableEpisode>
does not update.
if updates are wanted (when currently playing is displayed) then it will use CurrentlyPlaying

GetDisplayableEpisode
does not update.
if updates are wanted (when currently playing is displayed) then it will use CurrentlyPlaying

GetCurrentlyPlaying: Flow<CurrentlyPlaying?>

if I dont like this the alternative I can just have the feed update always but just at a lower frequency
 */
/**
 * Use case to get the displayable episodes from a podcast show
 */
interface GetDisplayableEpisodes {
    suspend operator fun invoke(
        show: PodcastShow? = null
    ): Catching<ImmutableList<DisplayableEpisode>>
}

/**
 * use case to get the displayable version of a specific episode
 */
fun interface GetDisplayableEpisode {
    suspend operator fun invoke(id: String): Catching<DisplayableEpisode>
}

/**
 * use case to get the currently playing displayable episode
 *
 * @returns a flow that is updated with the currently playing episode along with its state changes
 */
fun interface GetCurrentlyPlaying {
    operator fun invoke(): Flow<CurrentlyPlaying?>
}

fun interface IsEpisodeCurrentlyPlaying {
    operator fun invoke(id: String): Boolean
}