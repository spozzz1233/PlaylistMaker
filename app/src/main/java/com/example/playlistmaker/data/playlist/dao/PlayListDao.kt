package com.example.playlistmaker.data.playlist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.playlist.entity.PlayListEntity
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.domain.search.model.Track

@Dao
interface PlayListDao {
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayList (playList: PlayListEntity)

    @Query("SELECT * FROM PlayList_table")
    fun getPlayList(): List <PlayListEntity>
}

