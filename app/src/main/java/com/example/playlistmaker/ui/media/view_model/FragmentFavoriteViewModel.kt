package com.example.playlistmaker.ui.media.view_model

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FragmentFavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor,

    ) : ViewModel() {
    private val _isFavoriteEmpty = MutableLiveData<Boolean>()
    val isFavoriteEmpty: LiveData<Boolean> = _isFavoriteEmpty
    fun getFavoriteTracks(): Flow<List<Track>> {
        viewModelScope.launch {
            favoriteInteractor.getFavorite().collect() { favoriteTracks ->
                if (favoriteTracks.isEmpty()) {
                    _isFavoriteEmpty.value = true
                } else {
                    _isFavoriteEmpty.value = false
                }
            }
        }
        return favoriteInteractor.getFavorite()
    }

}