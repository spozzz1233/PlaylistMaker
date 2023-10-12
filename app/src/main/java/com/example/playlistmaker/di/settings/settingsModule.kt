package com.example.playlistmaker.di.settings


import SettingsFragmentViewModel
import com.example.playlistmaker.App
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.theme.ThemeRepository
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor

import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl

import com.example.practicum.playlist.domain.sharing.impl.SharingInteractorImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {



    single<SettingsInteractor>{//SettingsRepository
        SettingsInteractorImpl(get())
    }

    single<SettingsRepository> {//ThemeRepository
        SettingsRepositoryImpl(get())
    }

    single<SharingInteractor>{//ExternalNavigator
        SharingInteractorImpl(get(),get())
    }



    single{
        ThemeRepository(get())
    }



    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    viewModel {
        SettingsFragmentViewModel(get(), get())
    }
}