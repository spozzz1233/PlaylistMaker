package com.example.playlistmaker.creator

import com.example.playlistmaker.data.MediaRepositoryImpl
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.MediaInteractor
import com.example.playlistmaker.domain.MediaInteractorImpl
import com.example.playlistmaker.domain.MediaRepository
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel


object Creator {
    private fun getMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }
    fun provideMediaUseCase(): MediaInteractor {
        return MediaInteractorImpl(getMediaRepository())
    }
    fun provideExternalNavigator(context: SettingsViewModel) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

}