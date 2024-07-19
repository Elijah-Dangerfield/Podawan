package com.dangerfield.features.playlist.internal

import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistRepository
import com.dangerfield.libraries.podcast.Episode
import com.dangerfield.libraries.podcast.PodcastRepository
import com.dangerfield.features.playlist.storage.PlaylistDao
import com.dangerfield.features.playlist.storage.PlaylistEntity
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import podawan.core.Catching
import podawan.core.ignoreValue
import podawan.core.logOnFailure
import podawan.core.throwIfDebug
import se.ansman.dagger.auto.AutoBind
import se.ansman.dagger.auto.AutoInitialize
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
@AutoInitialize
class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    @AppScope private val appScope: CoroutineScope
) : PlaylistRepository {

    private val playlistsFlow = flow {

        val dbResponse = Catching { playlistDao.getPlaylistsFlow() }

        dbResponse
            .onSuccess { playlistsFlow ->
                val playlists = playlistsFlow.map { playlistEntities ->
                    playlistEntities.map { playlistEntity ->
                        playlistEntity.toPlaylist()
                    }
                }

                emitAll(playlists)
            }
            .logOnFailure()
            .throwIfDebug()
    }
        .stateIn(
            scope = appScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override suspend fun getPlaylists(): Catching<List<Playlist>> = Catching {
        playlistsFlow.filterNotNull().first()
    }

    override suspend fun getPlaylist(id: Int): Catching<Playlist> = getPlaylists().map {
        it.first { playlist -> playlist.id == id }
    }

    override suspend fun getPlaylistFlow(id: Int): Catching<Flow<Playlist>> = Catching {
        playlistsFlow.filterNotNull().mapNotNull {
            it.find { playlist -> playlist.id == id }
        }
    }

    override suspend fun updatePlaylist(
        id: Int,
        name: String,
        description: String,
        imageUrl: String?
    ): Catching<Unit> = Catching {
        val playlist = playlistDao.getPlaylist(id)
            ?: throw IllegalArgumentException("Playlist with id $id not found")

        val updatedPlaylist = playlist.copy(
            name = name,
            description = description,
            imageUrl = imageUrl
        )

        playlistDao.updatePlaylist(updatedPlaylist)
    }

    override suspend fun getPlaylistsFlow(): Catching<Flow<List<Playlist>>> = Catching {
        playlistsFlow.filterNotNull()
    }

    override suspend fun createPlaylist(name: String): Catching<Int> = Catching {
        val result = playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = "",
                imageUrl = null,
                episodeIds = emptyList()
            )
        )

        result.toInt()
    }

    override suspend fun deletePlaylist(id: Int) {
        playlistDao.deletePlaylist(id)
    }

    override suspend fun addEpisodeToPlaylist(playlistId: Int, episodeId: String) {
        val playlist = playlistDao.getPlaylist(playlistId)

        if (playlist == null) {
            Timber.e("Playlist with id $playlistId not found")
            return
        }

        if (playlist.episodeIds.contains(episodeId)) {
            Timber.i("Episode with id $episodeId already exists in playlist with id $playlistId")
            return
        }

        val updatedPlaylist = playlist.copy(episodeIds = playlist.episodeIds + episodeId)

        playlistDao.updatePlaylist(updatedPlaylist)
    }

    override suspend fun removeEpisodeFromPlaylist(playlistId: Int, episodeId: String) {
        val playlist = playlistDao.getPlaylist(playlistId)

        if (playlist == null) {
            Timber.e("Playlist with id $playlistId not found")
            return
        }

        if (!playlist.episodeIds.contains(episodeId)) {
            Timber.i("Episode with id $episodeId does not exist in playlist with id $playlistId")
            return
        }

        val updatedPlaylist = playlist.copy(episodeIds = playlist.episodeIds - episodeId)

        playlistDao.updatePlaylist(updatedPlaylist)
    }

    private fun PlaylistEntity.toPlaylist(): Playlist {
        return Playlist(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            episodeIds = episodeIds
        )
    }
}