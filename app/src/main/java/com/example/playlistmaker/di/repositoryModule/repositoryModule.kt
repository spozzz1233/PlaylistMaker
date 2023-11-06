package com.example.playlistmaker.di.repositoryModule

import com.example.playlistmaker.data.converters.TrackConverter
import org.koin.dsl.module

val repositoryModule = module {
    factory { TrackConverter() }
}