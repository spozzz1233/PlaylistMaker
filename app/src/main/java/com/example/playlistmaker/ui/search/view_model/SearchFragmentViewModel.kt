package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.model.tracks
import com.example.playlistmaker.domain.search.SearchInteractor

class SearchFragmentViewModel(
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





    fun searchTrack(query: String) {
        if(query.isNotEmpty()){
            _loadingLiveData.value = true
        }
        searchInteractor.searchTrack(query) { success ->
            if (success) {
                if (tracks.isEmpty()) {
                    _loadingLiveData.value = false
                    _searchResultsLiveData.value = false
                    _noResultLiveData.value = true
                } else{
                    _loadingLiveData.value = false
                    _searchResultsLiveData.value = true
                    _noResultLiveData.value = false
                }
            } else {
                _loadingLiveData.value = false
                _searchResultsLiveData.value = false
                _noResultLiveData.value = false
                _noInternetLiveData.value = true
            }
        }
    }
}



