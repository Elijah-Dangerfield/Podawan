package com.dangerfield.features.playlist

interface MapToPlaylistWithEpisodes {
    suspend operator fun invoke(playlist: Playlist): PlaylistWithEpisodes
}