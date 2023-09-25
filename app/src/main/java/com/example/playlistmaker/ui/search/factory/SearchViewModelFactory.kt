package com.example.playlistmaker.ui.search.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.callback.SearchCallback
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

class SearchViewModelFactory(searchCallback: SearchCallback, context: Context) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchViewModel(searchRepository) as T
    }

    val searchRepository = Creator.provideSearchRepository(searchCallback, context)
}