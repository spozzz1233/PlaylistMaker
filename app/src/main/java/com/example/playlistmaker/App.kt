package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.dataModule.dataModule
import com.example.playlistmaker.di.favoriteModule.favoriteModule
import com.example.playlistmaker.di.mediaLibrary.mediaModule
import com.example.playlistmaker.di.player.playerModule
import com.example.playlistmaker.di.repositoryModule.repositoryModule
import com.example.playlistmaker.di.search.searchModule
import com.example.playlistmaker.di.settings.settingsModule
import com.example.playlistmaker.domain.settings.SettingsInteractor
import org.koin.android.ext.android.getKoin
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
                mediaModule,
                dataModule,
                repositoryModule,
                favoriteModule
            )
        }
        val settingsInteractor: SettingsInteractor = getKoin().get()
        val darkTheme = settingsInteractor.getThemeSettings()

        settingsInteractor.updateThemeSetting(darkTheme)
    }
}