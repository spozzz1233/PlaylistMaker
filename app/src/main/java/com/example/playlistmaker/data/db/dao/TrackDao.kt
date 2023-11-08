package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert (entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack (track: Track)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack (track:TrackEntity)

    @Query("SELECT * FROM track_table WHERE isFavorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE trackId=:id")
    fun checkTreckInFavorite(id: Long):TrackEntity
}