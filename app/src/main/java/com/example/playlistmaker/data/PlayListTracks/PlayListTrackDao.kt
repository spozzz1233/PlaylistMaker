package com.example.playlistmaker.data.PlayListTracks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.playlist.entity.PlayListEntity
import com.example.playlistmaker.domain.search.model.Track
import retrofit2.http.DELETE

@Dao
interface PlayListTrackDao {
    @Insert(entity = PlayListTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack(track: Track)

    @Query("SELECT * FROM track_in_playlist_table WHERE trackId=:searchId")
    fun queryTrackId(searchId: Long): PlayListTracksEntity?

    @Query("DELETE FROM track_in_playlist_table WHERE trackId = :id")
    fun deleteIfisNotInPlaylist(id: Long)

    @Query("SELECT trackId FROM track_in_playlist_table")
    fun getTrackId(): List<Long>
}