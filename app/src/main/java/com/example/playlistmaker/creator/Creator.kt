package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.api.SearchApi
import com.example.playlistmaker.data.player.impl.MediaRepositoryImpl
import com.example.playlistmaker.data.search.Impl.SearchRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.theme.ThemeRepository
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.callback.SearchCallback
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.player.impl.MediaInteractorImpl
import com.example.playlistmaker.domain.player.MediaRepository
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.practicum.playlist.domain.sharing.impl.SharingInteractorImpl


object Creator {
    private fun getMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }
    fun provideMediaInteractor(): MediaInteractor {
        return MediaInteractorImpl(getMediaRepository())
    }
    fun getExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun getSettingsRepository(context: Context): SettingsRepository{
        return SettingsRepositoryImpl(ThemeRepository(context))
    }
    fun getSettingsInteractor(context: Context): SettingsInteractor{
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context),context)
    }
    fun getSearchRepository(searchCallback: SearchCallback , context: Context):SearchRepository{
        return SearchRepositoryImpl(context,searchCallback)
    }
    fun provideSearchInteractor(searchCallback: SearchCallback , context: Context): SearchInteractor{
        return SearchInteractorImpl(getSearchRepository(searchCallback,context))
    }



}