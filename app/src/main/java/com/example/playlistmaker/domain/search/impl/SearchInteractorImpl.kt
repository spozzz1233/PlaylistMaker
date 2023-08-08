package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository): SearchInteractor {
    override fun searchTrack(query: String) {
        searchRepository.searchTrack(query)
    }

    override fun getHistoryTracks() {
        searchRepository.getHistoryTracks()
    }

    override fun checkInternetConnection(): Boolean {
        return searchRepository.checkInternetConnection()
    }
}