package com.example.playlistmaker.data.search

import androidx.appcompat.app.AppCompatDelegate


interface SearchRepository {
    fun searchTrack(query: String, callback: (success: Boolean) -> Unit)
    fun getHistoryTracks()
    fun checkInternetConnection(): Boolean

}