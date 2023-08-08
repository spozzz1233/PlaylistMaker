package com.example.playlistmaker.domain.settings.model

sealed class ThemeSettings {
    abstract fun copy(isDarkTheme: Boolean): ThemeSettings

    object LightTheme : ThemeSettings() {
        override fun copy(isDarkTheme: Boolean): ThemeSettings {
            return if (isDarkTheme) DarkTheme else this
        }
    }

    object DarkTheme : ThemeSettings() {
        override fun copy(isDarkTheme: Boolean): ThemeSettings {
            return if (isDarkTheme) this else LightTheme
        }
    }
}


