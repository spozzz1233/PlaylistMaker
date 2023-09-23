package com.example.playlistmaker.domain.search

interface SearchInteractor {
    fun searchTrack(query: String, callback: (success: Boolean) -> Unit)
    fun getHistoryTracks()
    fun checkInternetConnection(): Boolean
}
