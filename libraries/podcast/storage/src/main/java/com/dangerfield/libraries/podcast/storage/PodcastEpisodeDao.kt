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

    @Query("UPDATE podcast_episodes SET resumePointSeconds = :resumePointSeconds WHERE guid = :id")
    suspend fun updateResumePoint(id: String, resumePointSeconds: Int)

    @Query("UPDATE podcast_episodes SET totalDurationSeconds = :durationSeconds WHERE guid = :id")
    suspend fun updateDuration(id: String, durationSeconds: Int)

    @Delete
    suspend fun deleteEpisode(episode: PodcastEpisodeEntity)

    @Query("SELECT * FROM podcast_episodes WHERE showRssFeedLink = :rssFeedLink")
    suspend fun getEpisodesForShow(rssFeedLink: String): List<PodcastEpisodeEntity>

    @Query("SELECT * FROM podcast_episodes WHERE guid = :guid")
    suspend fun getEpisodeByGuid(guid: String): PodcastEpisodeEntity?
}