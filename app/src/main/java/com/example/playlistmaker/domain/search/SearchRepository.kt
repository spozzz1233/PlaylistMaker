package com.example.playlistmaker.domain.search



interface SearchRepository {
    fun searchTrack(query: String)
    fun getHistoryTracks()
    fun checkInternetConnection(): Boolean
}