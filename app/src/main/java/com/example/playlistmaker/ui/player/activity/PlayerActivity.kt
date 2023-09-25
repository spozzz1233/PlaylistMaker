package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var playButton: ImageView
    private lateinit var progressOfTheWork: TextView
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        back = findViewById(R.id.back)
        playButton = findViewById(R.id.play_button)
        progressOfTheWork = findViewById(R.id.progress_of_the_work)

        back.setOnClickListener {
            finish()
        }




        val track = intent.getStringExtra("trackName")
        val artist = intent.getStringExtra("artistName")
        val trackTimeMillis = intent.getIntExtra("trackTimeMillis", 0)
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")

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
            .load(artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_512)
            .error(R.drawable.placeholder_512)
            .transform(RoundedCorners(8))
            .into(image)

        if (!collectionName.isNullOrEmpty()) {
            collection.visibility = View.VISIBLE
            album.visibility = View.VISIBLE
        }

        if (!primaryGenreName.isNullOrEmpty()) {
            genre.visibility = View.VISIBLE
            genre_name.visibility = View.VISIBLE
        }

        val trackUrl = intent.getStringExtra(TRACK_URL)!!

        viewModel.preparePlayer(trackUrl) {
            playButton.isEnabled = true
        }

        playButton.setOnClickListener {
            playbackControl()
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            if (isPlaying) {
                playButton.setImageResource(R.drawable.button_pause)
            } else {
                playButton.setImageResource(R.drawable.button_play)
            }
        }


        updateTime()
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.isPlaying()) {
            viewModel.pausePlayer { }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPlayer()
    }

    private fun playbackControl() {
        if (viewModel.isPlaying()) {
            viewModel.pausePlayer {}
        } else {
            viewModel.startPlayer {}
        }
    }

    private fun updateTime() {
        if (viewModel.isPlaying()) {
            val currentPosition = viewModel.getCurrentPosition()
            val text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
            progressOfTheWork.text = text
        }
        progressOfTheWork.postDelayed(::updateTime, 400)
    }


    companion object{
        const val TRACK_URL = "trackUrl"
    }
}













