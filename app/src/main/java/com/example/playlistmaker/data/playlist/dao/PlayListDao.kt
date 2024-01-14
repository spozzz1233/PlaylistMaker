package com.example.playlistmaker.data.playlist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.playlist.entity.PlayListEntity
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.domain.search.model.Track

@Dao
interface PlayListDao {
    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayList(playList: PlayListEntity)

    @Query("SELECT * FROM PlayList_table")
    fun getPlayList(): List<PlayListEntity>

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: PlayListEntity)

    @Query("SELECT * FROM PlayList_table WHERE playlistId =:id")
    fun getUpdatePlayList(id: Int): PlayListEntity

    @Delete(entity = PlayListEntity::class)
    fun deletePlaylist(playlist: PlayListEntity)
}

