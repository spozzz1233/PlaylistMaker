package com.example.playlistmaker.domain

import com.example.playlistmaker.data.MediaRepositoryImpl

object Creator {
    fun provideMediaUseCase(): MediaUseCase {
        val mediaRepository = MediaRepositoryImpl()
        return MediaUseCase(mediaRepository)
    }

}