package com.dangerfield.libraries.podcast.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PodcastEpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: PodcastEpisodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<PodcastEpisodeEntity>)

    @Update
    suspend fun updateEpisode(episode: PodcastEpisodeEntity)

    @Delete
    suspend fun deleteEpisode(episode: PodcastEpisodeEntity)

    @Query("SELECT * FROM podcast_episodes WHERE showRssFeedLink = :rssFeedLink")
    suspend fun getEpisodesForShow(rssFeedLink: String): List<PodcastEpisodeEntity>

    @Query("SELECT * FROM podcast_episodes WHERE guid = :guid")
    suspend fun getEpisodeByGuid(guid: String): PodcastEpisodeEntity?
}