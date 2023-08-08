package com.example.playlistmaker.domain.model


data class Track(
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


var tracks = ArrayList<Track>()


// Call the addHistoryTracks function to add elements to the historyTracks list
