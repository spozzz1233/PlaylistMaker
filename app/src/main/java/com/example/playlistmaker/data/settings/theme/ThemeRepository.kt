package com.example.playlistmaker.data.settings.theme

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class ThemeRepository(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "ThemePrefs"
        private const val KEY_DARK_THEME = "isDarkTheme"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getThemeSettings(): ThemeSettings {
        val isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
        return if (isDarkTheme) ThemeSettings.DarkTheme else ThemeSettings.LightTheme
    }

    fun updateThemeSettings(themeSettings: ThemeSettings) {
        val isDarkTheme = when (themeSettings) {
            is ThemeSettings.DarkTheme -> true
            is ThemeSettings.LightTheme -> false
        }
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_DARK_THEME, isDarkTheme)
        editor.apply()
    }
}
