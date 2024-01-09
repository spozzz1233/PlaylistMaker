package com.example.playlistmaker.data.playlist.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlayList_table")
data class PlayListEntity (
    @PrimaryKey(autoGenerate = true)
    val playlistId:Int?,
    val playlistName:String,
    val description:String?,
    val uri:String,
    val trackList:String?,
    val arrayNumber:Int?
)