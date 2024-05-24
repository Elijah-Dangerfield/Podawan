package com.dangerfield.features.playlist

import com.dangerfield.libraries.podcast.Episode
import kotlinx.coroutines.flow.Flow
import podawan.core.Catching

interface PlaylistRepository {

    suspend fun getPlaylists(): Catching<List<Playlist>>

    suspend fun getPlaylistsFlow(): Catching<Flow<List<Playlist>>>

    suspend fun createPlaylist(name: String): Catching<Unit>

    suspend fun deletePlaylist(id: Int)

    suspend fun addEpisodeToPlaylist(playlistId: Int, episode: Episode)
}