package com.dangerfield.libraries.podcast.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HeroImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeroImage(image: HeroImageEntity): Long

    @Update
    suspend fun updateHeroImage(image: HeroImageEntity)

    @Delete
    suspend fun deleteHeroImage(image: HeroImageEntity)

    @Query("SELECT * FROM hero_images WHERE id = :id")
    suspend fun getHeroImageById(id: Long): HeroImageEntity?
}