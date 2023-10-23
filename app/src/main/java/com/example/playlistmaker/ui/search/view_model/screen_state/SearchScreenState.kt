package com.example.playlistmaker.ui.search.view_model.screen_state

import com.example.playlistmaker.domain.search.model.Track

sealed class SearchScreenState {
    object Search : SearchScreenState()
    object Loading : SearchScreenState()
    object NoResultError : SearchScreenState()
    object ConnectionError : SearchScreenState()
    data class SearchWithHistory(var historyData: List<Track>) : SearchScreenState()
    data class SearchIsOk(val data: List<Track>) : SearchScreenState()
}