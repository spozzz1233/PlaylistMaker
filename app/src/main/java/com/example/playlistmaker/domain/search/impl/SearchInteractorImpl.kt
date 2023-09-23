package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.data.search.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository): SearchInteractor {
    override fun searchTrack(
        query: String,
        callback: (success: Boolean) -> Unit
    ) {
        searchRepository.searchTrack(query, callback) // Поменяйте местами query и callback
    }


    override fun getHistoryTracks() {
        searchRepository.getHistoryTracks()
    }

    override fun checkInternetConnection(): Boolean {
        return searchRepository.checkInternetConnection()
    }
}

