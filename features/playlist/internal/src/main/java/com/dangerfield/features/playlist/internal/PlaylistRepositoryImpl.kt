package com.dangerfield.features.playlist.internal

import com.dangerfield.features.playlist.Playlist
import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.features.playlist.storage.PlaylistDao
import com.dangerfield.features.playlist.storage.PlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao, // I have a hunch that this is not being provided
    private val podcastRepository: PodcastRepository
) : com.dangerfield.features.playlist.PlaylistRepository {
    override suspend fun getPlaylists(): Catching<List<Playlist>> = Catching {
        playlistDao.getPlaylists().map { playlistEntity ->
            playlistEntity.toPlaylist()
        }
    }

    private suspend fun PlaylistEntity.toPlaylist(): Playlist {
        val results = this.episodeIds.map { id -> podcastRepository.getEpisode(id) }
        val episodes = results.map { it.getOrThrow() }

        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            episodes = episodes
        )
    }

    override suspend fun getPlaylistsFlow(): Catching<Flow<List<Playlist>>> = Catching {
        playlistDao.getPlaylistsFlow().map { playlists ->
            playlists.map { it.toPlaylist() }
        }
    }

    override suspend fun createPlaylist(name: String): Catching<Unit> = Catching {
        playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = "",
                imageUrl = null,
                episodeIds = emptyList()
            )
        )
    }

    override suspend fun deletePlaylist(id: Int) {
        playlistDao.deletePlaylist(id)
    }

    override suspend fun addEpisodeToPlaylist(playlistId: Int, episode: Episode) {
        val playlist = playlistDao.getPlaylist(playlistId)

        if (playlist == null) {
            Timber.e("Playlist with id $playlistId not found")
            return
        }

        val updatedPlaylist = playlist.copy(episodeIds = playlist.episodeIds + episode.guid)

        playlistDao.updatePlaylist(updatedPlaylist)
    }
}