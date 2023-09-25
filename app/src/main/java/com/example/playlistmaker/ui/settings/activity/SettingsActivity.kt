package com.example.playlistmaker.ui.settings.activity

import SettingsViewModel
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {


    private val vm by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<ImageView>(R.id.back)
        val share = findViewById<TextView>(R.id.share)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val support = findViewById<TextView>(R.id.Support)
        val terms = findViewById<TextView>(R.id.terms)





        back.setOnClickListener {
            finish()
        }

        share.setOnClickListener{
            vm.share()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            vm.updateThemeSettings(isChecked)
        }

        support.setOnClickListener{
            vm.support()
        }

        terms.setOnClickListener{
            vm.terms()
        }


        vm.themeSettingsLiveData.observe(this, { themeSettings ->
            themeSwitcher.isChecked = when (themeSettings) {
                is ThemeSettings.LightTheme -> false
                is ThemeSettings.DarkTheme -> true
            }
        })
    }
}

