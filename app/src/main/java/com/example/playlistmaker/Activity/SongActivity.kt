package com.example.playlistmaker.Activity

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.*

class SongActivity : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var playButton:ImageView
    private lateinit var progressOfTheWork:TextView
    private var mediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())
    private var updateTimeRunnable: Runnable = Runnable { }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        back = findViewById(R.id.back)
        playButton = findViewById(R.id.play_button)
        back.setOnClickListener {
            finish()
        }
        progressOfTheWork = findViewById(R.id.progress_of_the_work)
        val track = intent.getStringExtra("trackName")
        val artist = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getIntExtra("trackTimeMillis",0)
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val trackUrl = intent.getStringExtra("trackUrl")

        val trackName: TextView = findViewById(R.id.trackName)
        val artistName: TextView = findViewById(R.id.artistName)
        val genre: TextView = findViewById(R.id.Genre_name)
        val image: ImageView = findViewById(R.id.track_cover)
        val collection: TextView = findViewById(R.id.album_name)
        val date: TextView = findViewById(R.id.year_name)
        val trackTime: TextView = findViewById(R.id.duration_name)
        val genre_name: TextView = findViewById(R.id.Genre)

        val countryTextView: TextView = findViewById(R.id.country_name)
        val album: TextView = findViewById(R.id.album)
        trackName.text = track
        artistName.text = artist
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val dateObj = dateFormat.parse(releaseDate)
        val formattedDate = SimpleDateFormat("yyyy", Locale.getDefault()).format(dateObj)
        date.text = formattedDate

        collection.text = collectionName
        genre.text = primaryGenreName
        countryTextView.text = country

        Glide.with(this)
            .load(artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_512)
            .error(R.drawable.placeholder_512)
            .transform(RoundedCorners(8))
            .into(image)

        if (collectionName != null) {
            collection.visibility = View.VISIBLE
            album.visibility = View.VISIBLE
        }
        if (primaryGenreName != null) {
            genre.visibility = View.VISIBLE
            genre_name.visibility = View.VISIBLE
        }
        preparePlayer()
        playButton.setOnClickListener {
            playbackControl()
        }
        updateTime()
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
    private fun preparePlayer() {
        val trackUrl = intent.getStringExtra("trackUrl")
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.button_play)
            playerState = STATE_PREPARED
        }
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun updateTime() {
        if (mediaPlayer != null && mediaPlayer.isPlaying) {
            val currentPosition = mediaPlayer.currentPosition
            val text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            progressOfTheWork.text = text
        }
        updateTimeRunnable = Runnable { updateTime() }
        handler.postDelayed(updateTimeRunnable, 400)
    }


}
