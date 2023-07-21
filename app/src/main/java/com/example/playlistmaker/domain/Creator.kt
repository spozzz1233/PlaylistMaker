package com.example.playlistmaker.domain

import com.example.playlistmaker.data.MediaRepository
import com.example.playlistmaker.data.MediaRepositoryImpl

object Creator {
    private fun getMediaRepository(): MediaRepository {
        return MediaRepositoryImpl()
    }
    fun provideMediaUseCase(): MediaInteractor {
        return MediaInteractorImpl(getMediaRepository())
    }

}