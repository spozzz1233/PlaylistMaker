package com.example.playlistmaker.ui.settings.factory
import SettingsViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator



class SettingsViewModelFactory(context: Context): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsInteractor,sharingInteractor) as T
    }
    val settingsInteractor = Creator.getSettingsInteractor(context)
    val sharingInteractor = Creator.provideSharingInteractor(context)
}
