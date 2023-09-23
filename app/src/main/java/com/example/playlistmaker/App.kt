package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.mediaLibrary.mediaModule
import com.example.playlistmaker.di.player.playerModule
import com.example.playlistmaker.di.search.searchModule
import com.example.playlistmaker.di.settings.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(settingsModule,
                playerModule,
                searchModule,
                mediaModule
            )
        }
    }

    var darkTheme = false

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}