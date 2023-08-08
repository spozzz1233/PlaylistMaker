package com.example.playlistmaker.ui.search.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.callback.SearchCallback
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.SearchRepository

class SearchViewModel(
    val searchInteractor: SearchInteractor
): ViewModel() {

    fun searchTrack(query: String) {
        searchInteractor.searchTrack(query)
    }

}