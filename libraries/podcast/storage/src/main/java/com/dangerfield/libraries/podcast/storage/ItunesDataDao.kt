package com.dangerfield.libraries.podcast.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItunesDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannelData(data: ItunesChannelDataEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodeData(data: EpisodeItunesDataEntity): Long

    @Query("SELECT * FROM itunes_channel_data WHERE id = :id")
    suspend fun getChannelDataById(id: Long): ItunesChannelDataEntity?

    @Query("SELECT * FROM itunes_episode_data WHERE id = :id")
    suspend fun getEpisodeDataById(id: Long): EpisodeItunesDataEntity?
}