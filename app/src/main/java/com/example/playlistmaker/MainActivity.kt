package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val search = findViewById<Button>(R.id.search)
        val media = findViewById<Button>(R.id.media)
        val settings = findViewById<Button>(R.id.settings)
        val musicHistory = MusicHistory(this)
        musicHistory.getHistory()
        search.setOnClickListener {
            val searchIntent = Intent(this,SearchActivity::class.java)
            startActivity(searchIntent)
        }
        media.setOnClickListener {
            val mediatekaIntent = Intent(this,MediatekaActivity::class.java)
            startActivity(mediatekaIntent)
        }
        settings.setOnClickListener {
            val settingsIntent = Intent(this,SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}