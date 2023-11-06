package com.example.playlistmaker.di.favoriteModule

import com.example.playlistmaker.data.db.FavoriteRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.FavoriteInteractorImpl
import com.example.playlistmaker.domain.db.FavoriteRepository
import org.koin.dsl.module

val favoriteModule = module{

    single<FavoriteRepository>{
        FavoriteRepositoryImpl(get(),get())
    }
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

}