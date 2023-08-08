package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel(){
    val externalNavigator = Creator.provideExternalNavigator(this)

//    fun darkTheme(): Boolean{
//        return switcher, checked ->
//        (applicationContext as App).switchTheme(checked)
//    }
    fun terms(){
        externalNavigator.openLink()
    }
    fun share(){
        externalNavigator.shareLink()
    }
}