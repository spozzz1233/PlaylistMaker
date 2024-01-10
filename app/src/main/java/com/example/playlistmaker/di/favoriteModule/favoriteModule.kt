package com.example.playlistmaker.di.favoriteModule

import com.example.playlistmaker.data.favorite.FavoriteRepositoryImpl
import com.example.playlistmaker.domain.favorite.FavoriteInteractor
import com.example.playlistmaker.domain.favorite.FavoriteInteractorImpl
import com.example.playlistmaker.domain.favorite.FavoriteRepository
import org.koin.dsl.module

val favoriteModule = module{

    single<FavoriteRepository>{
        FavoriteRepositoryImpl(get(),get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

}