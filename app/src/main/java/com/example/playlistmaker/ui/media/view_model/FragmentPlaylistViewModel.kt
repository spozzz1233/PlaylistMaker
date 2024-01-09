package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.model.Playlist
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylistViewModel(
    private val playListInteractor: PlayListInteractor
    ): ViewModel() {
    val playListList: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>()
    fun getPlaylists() {
        viewModelScope.launch {
            playListInteractor.getPlayList()
                .collect {
                    if (it.isNotEmpty()) {
                        playListList.postValue(it)
                    } else {
                        playListList.postValue(emptyList())
                    }
                }
        }
    }
}