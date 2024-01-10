package com.example.playlistmaker.di.mediaLibrary

import com.example.playlistmaker.ui.media.view_model.FragmentCreatePlayListViewModel
import com.example.playlistmaker.ui.media.view_model.FragmentFavoriteViewModel
import com.example.playlistmaker.ui.media.view_model.FragmentPlaylistViewModel
import com.example.playlistmaker.ui.media.view_model.MediatekaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mediaModule = module {
    viewModel {
        FragmentFavoriteViewModel(get())
    }
    viewModel {
        FragmentCreatePlayListViewModel(get())
    }
    viewModel {
        MediatekaViewModel()
    }
    viewModel {
        FragmentPlaylistViewModel(get())
    }

}