package com.example.playlistmaker.ui.settings.activity


import SettingsViewModel
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.ui.settings.factory.SettingsViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var vm: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val back = findViewById<ImageView>(R.id.back)
        val share = findViewById<TextView>(R.id.share)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val support = findViewById<TextView>(R.id.Support)
        val terms = findViewById<TextView>(R.id.terms)


        vm = ViewModelProvider(this, SettingsViewModelFactory(this)).get(SettingsViewModel::class.java)



        vm.themeSettingsLiveData.observe(this, { themeSettings ->
            themeSwitcher.isChecked = when (themeSettings) {
                is ThemeSettings.LightTheme -> false
                is ThemeSettings.DarkTheme -> true
            }
        })
        back.setOnClickListener {
            finish()
        }

        share.setOnClickListener{
            vm.share()
        }
//        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
//            (applicationContext as App).switchTheme(checked)
//
//        }
        // Обработчик для themeSwitcher
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            // Вызываем функцию в SettingsViewModel для обновления темы
            vm.updateThemeSettings(isChecked)
        }

        support.setOnClickListener{
            vm.support()
        }
        terms.setOnClickListener{
            vm.terms()
        }
    }
}