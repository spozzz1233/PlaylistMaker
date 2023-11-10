package com.example.playlistmaker.domain.search.model

data class HistoryTrack(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
)

var historyTracks = ArrayList<HistoryTrack>()