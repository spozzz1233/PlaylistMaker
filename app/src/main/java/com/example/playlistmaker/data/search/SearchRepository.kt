package com.example.playlistmaker.data.search



interface SearchRepository {
    fun searchTrack(query: String, callback: (success: Boolean) -> Unit)
    fun getHistoryTracks()
    fun checkInternetConnection(): Boolean
}