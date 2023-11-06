package com.example.playlistmaker.domain.search.model


data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    var isFavorite: Boolean = false
)




var tracks = ArrayList<Track>()


// Call the addHistoryTracks function to add elements to the historyTracks list
