package com.example.playlistmaker.settings.data

import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.data.settings.theme.ThemeRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class SettingsRepositoryImpl(private val themeRepository: ThemeRepository) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return themeRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        themeRepository.updateThemeSettings(settings)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeRepository.switchTheme(darkThemeEnabled)
    }
}
