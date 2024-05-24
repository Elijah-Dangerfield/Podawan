package com.dangerfield.libraries.podcast.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Entity(
    tableName = "podcast_shows",
    foreignKeys = [
        ForeignKey(
            entity = ItunesChannelDataEntity::class,
            parentColumns = ["id"],
            childColumns = ["itunesChannelDataId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = HeroImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["heroImageId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class PodcastShowEntity(
    @PrimaryKey val rssFeedLink: String,
    val title: String?,
    val link: String?,
    val heroImageId: Long?,  // Foreign key for HeroImage
    val description: String?,
    val lastBuildDate: String?,
    val updatePeriod: String?,
    val itunesChannelDataId: Long?  // Foreign key for ItunesChannelData
)

@Entity(tableName = "hero_images")
data class HeroImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String?,
    val url: String?,
    val link: String?,
    val description: String?
)

@Entity(
    tableName = "podcast_episodes",
    foreignKeys = [
        ForeignKey(
            entity = PodcastShowEntity::class,
            parentColumns = ["rssFeedLink"],
            childColumns = ["showRssFeedLink"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EpisodeItunesDataEntity::class,
            parentColumns = ["id"],
            childColumns = ["itunesItemDataId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = HeroImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["showHeroImageId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class PodcastEpisodeEntity(
    @PrimaryKey val guid: String,
    val showRssFeedLink: String,  // Foreign key to PodcastShow
    val title: String?,
    val author: String?,
    val link: String?,
    val pubDate: String?,
    val description: String?,
    val categories: List<String>,
    val content: String?,
    val image: String?,
    val audio: String?,
    val video: String?,
    val sourceName: String?,
    val sourceUrl: String?,
    val itunesItemDataId: Long?,  // Foreign key to EpisodeItunesData
    val commentsUrl: String?,
    val showHeroImageId: Long?,  // Foreign key for HeroImage,
    val resumePointSeconds: Int,
    val totalDurationSeconds: Int,
)

@Entity(tableName = "itunes_episode_data")
data class EpisodeItunesDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val author: String?,
    val duration: String?,
    val episode: String?,
    val episodeType: String?,
    val keywords: List<String>,
    val explicit: String?,
    val image: String?,
    val subtitle: String?,
    val summary: String?,
    val season: String?
)


@Entity(tableName = "itunes_channel_data")
data class ItunesChannelDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val author: String?,
    val duration: String?,
    val explicit: String?,
    val image: String?,
    val newsFeedUrl: String?,
    val categories: List<String>,
    val keywords: List<String>,
    val subtitle: String?,
    val summary: String?,
    val type: String?,
    val ownerName: String?,
    val ownerEmail: String?
)