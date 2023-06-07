package com.example.playlistmaker


data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)


var tracks = ArrayList<Track>()

data class HistoryTrack(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)

var historyTracks = ArrayList<HistoryTrack>()


// Call the addHistoryTracks function to add elements to the historyTracks list
