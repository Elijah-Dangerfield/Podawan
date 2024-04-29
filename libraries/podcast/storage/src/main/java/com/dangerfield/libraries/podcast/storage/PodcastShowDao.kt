package com.dangerfield.libraries.podcast.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PodcastShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(show: PodcastShowEntity): Long

    @Update
    suspend fun updateShow(show: PodcastShowEntity)

    @Delete
    suspend fun deleteShow(show: PodcastShowEntity)

    @Query("SELECT * FROM podcast_shows WHERE rssFeedLink = :link")
    suspend fun getShowWithRssLink(link: String): PodcastShowEntity?

}