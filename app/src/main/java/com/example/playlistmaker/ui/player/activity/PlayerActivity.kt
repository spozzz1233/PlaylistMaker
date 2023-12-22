package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.media.adapter.FragmentPlayListAdapter
import com.example.playlistmaker.ui.media.fragment.CreatePlayListFragment
import com.example.playlistmaker.ui.player.adapter.PlayerPlayListAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*



class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()
    lateinit var binding: ActivityPlayerBinding
    private lateinit var playerPlayListAdapter: PlayerPlayListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val recyclerView = binding.recyclerView
        playerPlayListAdapter = PlayerPlayListAdapter()
        recyclerView.adapter = playerPlayListAdapter
        binding.back.setOnClickListener {
            finish()
        }
        binding.addButton.setOnClickListener{
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        binding.newPlaylist.setOnClickListener {
            val createPlaylistFragment = CreatePlayListFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(android.R.id.content, createPlaylistFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        viewModel.getPlaylists()

        viewModel.playListList.observe(this) { it ->
            Log.d("playListList","$it")
            if (it.isNullOrEmpty()) {

                return@observe
            } else {
                recyclerView.adapter = playerPlayListAdapter
                playerPlayListAdapter.setItems(it)
                return@observe
            }
        }


        val track = intent.getStringExtra(ARGS_TRACK) ?: ""
        val trackIdSerializable = intent.getSerializableExtra(ARGS_TRACKID)
        val trackId = if (trackIdSerializable is Int) {
            trackIdSerializable
        } else {
            0
        }
        val artist = intent.getStringExtra(ARGS_ARTIST) ?: ""
        val trackTimeMillis = intent.getIntExtra(ARGS_TRACKTIMEMILLIS, 0)
        val artworkUrl100 = intent.getStringExtra(ARGS_ARTWORKURL100) ?: ""
        val collectionName = intent.getStringExtra(ARGS_COLLECTIONNAME) ?: ""
        val releaseDate = intent.getStringExtra(ARGS_RELEASEDATE) ?: ""
        val primaryGenreName = intent.getStringExtra(ARGS_PRIMARYGENRENAME) ?: ""
        val country = intent.getStringExtra(ARGS_COUNTRY) ?: ""
        val trackUrl = intent.getStringExtra(ARGS_TRACK_URL) ?: ""
        val addedTimestamp = System.currentTimeMillis()
        val countryTextView: TextView = findViewById(R.id.country_name)
        val album: TextView = findViewById(R.id.album)

        binding.trackName.text = track
        binding.artistName.text = artist
        binding.durationName.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val dateObj = dateFormat.parse(releaseDate)
        val formattedDate = SimpleDateFormat("yyyy", Locale.getDefault()).format(dateObj)
        binding.yearName.text = formattedDate

        binding.albumName.text = collectionName
        binding.GenreName.text = primaryGenreName
        countryTextView.text = country

        Glide.with(this)
            .load(artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_512)
            .error(R.drawable.placeholder_512)
            .transform(RoundedCorners(8))
            .into(binding.trackCover)

        if (!collectionName.isNullOrEmpty()) {
            binding.albumName.visibility = View.VISIBLE
            album.visibility = View.VISIBLE
        }

        if (!primaryGenreName.isNullOrEmpty()) {
            binding.GenreName.visibility = View.VISIBLE
            binding.Genre.visibility = View.VISIBLE
        }



        viewModel.preparePlayer(trackUrl) {
            binding.playButton.isEnabled = true
        }

        binding.playButton.setOnClickListener {
            playbackControl()
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            if (isPlaying) {
                binding.playButton.setImageResource(R.drawable.button_pause)
            } else {
                binding.playButton.setImageResource(R.drawable.button_play)
            }
        }

        var trackObject = Track(
            trackId,
            track,
            artist,
            trackTimeMillis,
            artworkUrl100,
            collectionName,
            releaseDate,
            primaryGenreName,
            country,
            trackUrl
        )


        viewModel.checkTrackInFavorite(trackObject)
            .observe(this) { favourtitesIndicator ->
                if (favourtitesIndicator){
                    trackObject.isFavorite = true
                    binding.buttonLike.setImageResource(R.drawable.button_like_red)
                }else {
                    binding.buttonLike.setImageResource(R.drawable.button_like)
                }
            }
        binding.buttonLike.setOnClickListener {
            viewModel.FavoriteTrack(trackObject)
        }

        startUpdatingTime()
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

    private var updateTimeJob: Job? = null

    private fun startUpdatingTime() {
        updateTimeJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val currentPosition = viewModel.getCurrentPosition()
                val playerState = viewModel.getPlayerState()
                if (viewModel.isPlaying()) {
                    val text = viewModel.formatTime(currentPosition)
                    withContext(Dispatchers.Main) {
                        binding.progressOfTheWork.text = text
                    }
                }else if (playerState == 1) {
                    binding.playButton.setImageResource(R.drawable.button_play)
                    binding.progressOfTheWork.text = "00:00"
                }

                delay(300)
            }
        }
    }

    companion object{
        private const val ARGS_TRACK_URL = "trackUrl"
        private const val ARGS_TRACKID = "trackId"
        private const val ARGS_TRACK = "trackName"
        private const val ARGS_ARTIST = "artistName"
        private const val ARGS_TRACKTIMEMILLIS = "trackTimeMillis"
        private const val ARGS_ARTWORKURL100 = "artworkUrl100"
        private const val ARGS_COLLECTIONNAME = "collectionName"
        private const val ARGS_RELEASEDATE = "releaseDate"
        private const val ARGS_PRIMARYGENRENAME = "primaryGenreName"
        private const val ARGS_COUNTRY = "country"

        fun createArgs(
            trackId: Int,
            trackName: String,
            trackUrl: String,
            artist: String,
            trackTimeMillis: Int,
            artworkUrl100: String,
            collectionName: String,
            releaseDate: String,
            primaryGenreName: String,
            country: String,

            ): Bundle =
            bundleOf(ARGS_TRACKID to trackId,
                ARGS_TRACK to trackName,
                ARGS_TRACK_URL to trackUrl,
                ARGS_ARTIST to artist,
                ARGS_TRACKTIMEMILLIS to trackTimeMillis,
                ARGS_ARTWORKURL100 to artworkUrl100,
                ARGS_COLLECTIONNAME to collectionName,
                ARGS_RELEASEDATE to releaseDate,
                ARGS_PRIMARYGENRENAME to primaryGenreName,
                ARGS_COUNTRY to country
                )
    }

}













