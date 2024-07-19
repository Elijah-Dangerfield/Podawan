package com.dangerfield.features.playlist

import com.dangerfield.libraries.podcast.Episode

data class  Playlist(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val episodeIds: List<String>,
)

data class PlaylistWithEpisodes(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val episodes: List<Episode>
)