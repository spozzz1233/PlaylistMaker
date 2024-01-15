package com.example.playlistmaker.data.PlayListTracks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlist_table")
data class PlayListTracksEntity(
    @PrimaryKey
    val trackId: Long,
    val addTime: Long?,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
)