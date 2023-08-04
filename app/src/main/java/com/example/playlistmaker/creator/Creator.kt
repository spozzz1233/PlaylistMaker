package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.MediaRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.theme.ThemeRepository
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.MediaInteractor
import com.example.playlistmaker.domain.MediaInteractorImpl
import com.example.playlistmaker.domain.MediaRepository
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
        return SharingInteractorImpl(getExternalNavigator(context))
    }

}