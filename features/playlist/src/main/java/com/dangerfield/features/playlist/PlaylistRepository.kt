package com.dangerfield.features.playlist

import kotlinx.coroutines.flow.Flow
import podawan.core.Catching

interface PlaylistRepository {

    suspend fun getPlaylists(): Catching<List<Playlist>>

    suspend fun getPlaylist(id: Int): Catching<Playlist>

    suspend fun updatePlaylist(
        id: Int,
        name: String,
        description: String,
        imageUrl: String?
    ): Catching<Unit>

    suspend fun getPlaylistFlow(id: Int): Catching<Flow<Playlist>>

    suspend fun getPlaylistsFlow(): Catching<Flow<List<Playlist>>>

    suspend fun createPlaylist(name: String): Catching<Int>

    suspend fun deletePlaylist(id: Int)

    suspend fun addEpisodeToPlaylist(playlistId: Int, episodeId: String)

    suspend fun removeEpisodeFromPlaylist(playlistId: Int,episodeId: String)
}