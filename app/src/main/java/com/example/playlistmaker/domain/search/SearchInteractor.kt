package com.example.playlistmaker.domain.search

interface SearchInteractor {
    fun searchTrack(query: String)
    fun getHistoryTracks()
    fun checkInternetConnection(): Boolean
}