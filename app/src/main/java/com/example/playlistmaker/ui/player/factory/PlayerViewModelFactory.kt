package com.example.playlistmaker.ui.player.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel

class PlayerViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PlayerViewModel(mediaInteractor) as T
    }
    val mediaInteractor = Creator.provideMediaUseCase()
}