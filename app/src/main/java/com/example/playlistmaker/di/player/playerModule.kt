package com.example.playlistmaker.di.player

import com.example.playlistmaker.data.player.MediaRepository
import com.example.playlistmaker.data.player.impl.MediaRepositoryImpl
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.player.impl.MediaInteractorImpl
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playerModule = module {

    single<MediaRepository>{
        MediaRepositoryImpl()
    }

    single<MediaInteractor>{
        MediaInteractorImpl(get())
    }

    viewModel {
        PlayerViewModel(get(),get())
    }
}