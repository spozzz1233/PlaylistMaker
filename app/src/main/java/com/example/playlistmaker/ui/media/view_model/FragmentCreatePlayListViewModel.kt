package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playList.PlayListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentCreatePlayListViewModel(
    private val playListInteractor: PlayListInteractor,
): ViewModel(){
    fun createPlayList(playlistName: String, description: String?, uri: String){
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.createPlayList(playlistName,description,uri)
        }
    }
}

