package com.example.playlistmaker.data.PlayListTracks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.domain.search.model.Track

@Dao
interface PlayListTrackDao {
    @Insert(entity = PlayListTracksEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insertTrack (track: Track)

}