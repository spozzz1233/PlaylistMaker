package com.example.playlistmaker.di.playListModule

import com.example.playlistmaker.data.converters.PlayListConverter
import com.example.playlistmaker.data.playlist.PlayListRepositoryImpl
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.PlayListInteractorImpl
import com.example.playlistmaker.domain.playList.PlayListRepository
import org.koin.dsl.module

val playListModule = module{

    single<PlayListRepository>{
        PlayListRepositoryImpl(get(),get(),get())
    }
    single<PlayListInteractor> {
        PlayListInteractorImpl(get())
    }
    single<PlayListConverter> {
        PlayListConverter()
    }

}