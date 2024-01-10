package com.example.playlistmaker.di.dataModule

import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.PlayListTrackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single {
        Room.databaseBuilder(androidContext(), PlayListTrackDatabase::class.java, "track_in_playlist_table")
            .build()
    }
}