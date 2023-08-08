package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.SearchRepository

class SearchViewModel(
    val searchRepository: SearchRepository
): ViewModel() {

    fun searchTrack(query: String) {
        searchRepository.searchTrack(query)
    }
}