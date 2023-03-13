package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val back = findViewById<ImageView>(R.id.back)
        val share = findViewById<TextView>(R.id.share)
        val theme = findViewById<TextView>(R.id.darkTheme)
        val support = findViewById<TextView>(R.id.Support)
        val terms = findViewById<TextView>(R.id.terms)

        back.setOnClickListener {
            finish()
        }

        share.setOnClickListener{
            val message = "Привет, приходи к нам на курс 'https://practicum.yandex.ru/profile/android-developer/'"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("minulin.misha@bk.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }
        theme.setOnClickListener{
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)} // Night mode is not active, we're using the light theme.
                Configuration.UI_MODE_NIGHT_YES -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)} // Night mode is active, we're using dark theme.
            }
        }
        support.setOnClickListener{
            val message1 = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message2 = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("minulin.misha@bk.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, message2)
            shareIntent.putExtra(Intent.EXTRA_TITLE, message1)
            startActivity(shareIntent)
        }
        terms.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(browserIntent)
        }


    }
}