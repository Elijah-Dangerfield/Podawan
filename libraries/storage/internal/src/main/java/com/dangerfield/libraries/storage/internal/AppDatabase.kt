package com.dangerfield.libraries.storage.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dangerfield.libraries.podcast.storage.EpisodeItunesDataEntity
import com.dangerfield.libraries.podcast.storage.HeroImageDao
import com.dangerfield.libraries.podcast.storage.HeroImageEntity
import com.dangerfield.libraries.podcast.storage.ItunesChannelDataEntity
import com.dangerfield.libraries.podcast.storage.ItunesDataDao
import com.dangerfield.features.playlist.storage.PlaylistDao
import com.dangerfield.features.playlist.storage.PlaylistEntity
import com.dangerfield.libraries.podcast.storage.PodcastEpisodeDao
import com.dangerfield.libraries.podcast.storage.PodcastEpisodeEntity
import com.dangerfield.libraries.podcast.storage.PodcastShowDao
import com.dangerfield.libraries.podcast.storage.PodcastShowEntity
import se.ansman.dagger.auto.androidx.room.AutoProvideDaos

@Database(
    entities = [
        PodcastEpisodeEntity::class,
        PodcastShowEntity::class,
        ItunesChannelDataEntity::class,
        HeroImageEntity::class,
        EpisodeItunesDataEntity::class,
        PlaylistEntity::class
    ],
    version = 1,
    exportSchema = true
)
@AutoProvideDaos
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val podcastDao: PodcastShowDao
    abstract val heroImageDao: HeroImageDao
    abstract val itunesDataDao: ItunesDataDao
    abstract val episodeDao: PodcastEpisodeDao
    abstract val playlistDao: PlaylistDao
}
