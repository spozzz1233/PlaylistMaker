package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.callback.SearchCallback

interface SearchRepository {
    fun searchTrack(query: String)
    fun getHistoryTracks()
    fun checkInternetConnection(): Boolean
}