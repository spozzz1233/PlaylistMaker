package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.tracks
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    private val _searchResultsListLiveData = MutableLiveData<ArrayList<Track>>()
    val searchResultsListLiveData: LiveData<ArrayList<Track>> = _searchResultsListLiveData


    fun clearSearch() {
        _loadingLiveData.value = false
        _searchResultsLiveData.value = false
        _noResultLiveData.value = false
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun searchDebounce(query: String) {
        if (latestSearchText == query) {
            return
        }

        latestSearchText = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack(query)
        }
    }




    fun searchTrack(query: String) {
        if (query.isNotEmpty()) {
            _loadingLiveData.value = true
            viewModelScope.launch {
                searchInteractor
                    .searchTrack(query)
                    .collect {
                        when(it.message){
                            ErrorType.CONNECTION_ERROR ->{
                                _loadingLiveData.value = false
                                _searchResultsLiveData.value = false
                                _noResultLiveData.value = false
                                _noInternetLiveData.value = true
                            }
                            else -> {processResult(it.data)}
                        }
                    }
            }
            _searchResultsListLiveData.value = tracks
        }
    }
    fun processResult(track: List<Track>?) {
        val tracks = ArrayList<Track>()
        if (track != null) {
            tracks.addAll(track)
            _searchResultsListLiveData.value = tracks
        }

        when {
            tracks.isEmpty() -> {
                _loadingLiveData.value = false
                _searchResultsLiveData.value = false
                _noResultLiveData.value = true
            }
            else -> {
                _loadingLiveData.value = false
                _searchResultsLiveData.value = true
                _noResultLiveData.value = false
            }
        }

    }
}

//searchInteractor.searchTrack(query) { success ->
//    if (success) {
//        if (tracks.isEmpty()) {
//            _loadingLiveData.value = false
//            _searchResultsLiveData.value = false
//            _noResultLiveData.value = true
//        } else{
//            _loadingLiveData.value = false
//            _searchResultsLiveData.value = true
//            _noResultLiveData.value = false
//        }
//    } else {
//        _loadingLiveData.value = false
//        _searchResultsLiveData.value = false
//        _noResultLiveData.value = false
//        _noInternetLiveData.value = true
//    }

