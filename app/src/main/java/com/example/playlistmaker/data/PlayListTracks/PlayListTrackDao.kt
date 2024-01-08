package com.example.playlistmaker.data.PlayListTracks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.domain.search.model.Track

@Dao
interface PlayListTrackDao {
    @Insert(entity = PlayListTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack (track: Track)
    @Query("SELECT * FROM track_in_playlist_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long): PlayListTracksEntity?

}