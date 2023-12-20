package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorite.FavoriteInteractor
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
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