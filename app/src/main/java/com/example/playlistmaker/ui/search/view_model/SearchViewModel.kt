package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.tracks
import com.example.playlistmaker.domain.search.SearchInteractor

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _searchResultsLiveData = MutableLiveData<Boolean>()
    val searchResultsLiveData: LiveData<Boolean> = _searchResultsLiveData

    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = _loadingLiveData

    private val _noResultLiveData = MutableLiveData<Boolean>()
    val noResultLiveData: LiveData<Boolean> = _noResultLiveData

    private val _noInternetLiveData = MutableLiveData<Boolean>()
    val noInternetLiveData: LiveData<Boolean> = _noInternetLiveData

    private val _historyLiveData = MutableLiveData<Boolean>()
    val historyLiveData: LiveData<Boolean> = _historyLiveData




    fun searchTrack(query: String) {
        searchInteractor.searchTrack(query) { success ->
            if (success) {
                if (tracks.isEmpty()) {
                    _searchResultsLiveData.value = false
                    _noResultLiveData.value = true
                } else if(tracks.isNotEmpty()){
                    _searchResultsLiveData.value = true
                }
            } else {
                _noInternetLiveData.value = true
            }
        }
    }
}



