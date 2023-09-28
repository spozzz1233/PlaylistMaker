import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsFragmentViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val app: App
) : ViewModel() {

    private val _themeSettingsLiveData: MutableLiveData<ThemeSettings> = MutableLiveData()
    val themeSettingsLiveData: LiveData<ThemeSettings> = _themeSettingsLiveData

    init {
        loadThemeSettings()
    }

    private fun loadThemeSettings() {
        val themeSettings = settingsInteractor.getThemeSettings()
        _themeSettingsLiveData.postValue(themeSettings) 
    }

    fun updateThemeSettings(isDarkTheme: Boolean) {
        val currentThemeSettings = _themeSettingsLiveData.value
        if (currentThemeSettings != null) {
            val updatedThemeSettings = currentThemeSettings.copy(isDarkTheme = isDarkTheme)
            settingsInteractor.updateThemeSetting(updatedThemeSettings)
            _themeSettingsLiveData.value = updatedThemeSettings
            settingsInteractor.switchTheme(isDarkTheme)
        }
    }



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

