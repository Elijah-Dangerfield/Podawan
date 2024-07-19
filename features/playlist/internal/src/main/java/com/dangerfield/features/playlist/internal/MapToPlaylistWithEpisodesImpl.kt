package com.dangerfield.features.playlist.internal

import com.dangerfield.features.playlist.MapToPlaylistWithEpisodes
import com.dangerfield.features.playlist.Playlist
import com.dangerfield.features.playlist.PlaylistWithEpisodes
import com.dangerfield.libraries.podcast.PodcastRepository
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
class MapToPlaylistWithEpisodesImpl @Inject constructor(
    private val podcastRepository: PodcastRepository
) : MapToPlaylistWithEpisodes {
    override suspend fun invoke(playlist: Playlist): PlaylistWithEpisodes {
        val episodes = podcastRepository.getEpisodes(playlist.episodeIds).getOrNull() ?: emptyList()

        return PlaylistWithEpisodes(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            imageUrl = playlist.imageUrl,
            episodes = episodes
        )
    }
}
