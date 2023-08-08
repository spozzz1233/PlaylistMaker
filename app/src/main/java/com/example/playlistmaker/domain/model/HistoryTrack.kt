package com.example.playlistmaker.domain.model

data class HistoryTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)

var historyTracks = ArrayList<HistoryTrack>()