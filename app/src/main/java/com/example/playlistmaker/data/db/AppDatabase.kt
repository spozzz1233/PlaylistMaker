package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.favorite.dao.TrackDao
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.data.playlist.dao.PlayListDao
import com.example.playlistmaker.data.playlist.entity.PlayListEntity


@Database(version = 1, entities = [TrackEntity::class,PlayListEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun TrackDao(): TrackDao
    abstract fun PlayListDao(): PlayListDao
}
