import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    // LiveData для хранения текущих настроек темы
    private val _themeSettingsLiveData: MutableLiveData<ThemeSettings> = MutableLiveData()
    val themeSettingsLiveData: LiveData<ThemeSettings>
        get() = _themeSettingsLiveData

    init {
        // При инициализации ViewModel, загружаем текущие настройки темы и обновляем LiveData
        loadThemeSettings()
    }

    private fun loadThemeSettings() {
        val themeSettings = settingsInteractor.getThemeSettings()
        _themeSettingsLiveData.value = themeSettings
    }

    // Вызывается, когда пользователь переключает тему
    fun updateThemeSettings(isDarkTheme: Boolean) {
        val currentThemeSettings = _themeSettingsLiveData.value
        if (currentThemeSettings != null) {
            val updatedThemeSettings = currentThemeSettings.copy(isDarkTheme = isDarkTheme)
            settingsInteractor.updateThemeSetting(updatedThemeSettings)
            _themeSettingsLiveData.value = updatedThemeSettings
        }
    }

    // Функции для обработки других действий пользователя (например, открытие терминов, поддержки и т. д.)

    fun terms() {
        sharingInteractor.openTerms()
    }

    fun share() {
        sharingInteractor.shareApp()
    }

    fun support() {
        sharingInteractor.openSupport()
    }
}
