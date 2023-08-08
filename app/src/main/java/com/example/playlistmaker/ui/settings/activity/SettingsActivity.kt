package com.example.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {


    val viewModel:SettingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
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
            viewModel.share()
        }
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
        support.setOnClickListener{
            val message1 = getString(R.string.header)
            val message2 = getString(R.string.mesage_support)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
            shareIntent.putExtra(Intent.EXTRA_TITLE, message1)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message2)
            startActivity(shareIntent)
        }
        terms.setOnClickListener{
            viewModel.terms()
        }
    }
}