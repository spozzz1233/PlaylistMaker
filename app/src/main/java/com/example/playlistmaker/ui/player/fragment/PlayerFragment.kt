package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.adapter.PlayerPlayListAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.*


class PlayerFragment : Fragment() {

    private val viewModel by viewModel<PlayerViewModel>()
    lateinit var binding: FragmentPlayerBinding
    private lateinit var playerPlayListAdapter: PlayerPlayListAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = arguments?.getParcelable<Track>("track")
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val recyclerView = binding.recyclerView
        var trackObject = track?.let {
            Track(
                it.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl
            )
        }
        playerPlayListAdapter = PlayerPlayListAdapter(emptyList()) { playlistList ->
            if (trackObject != null) {
                playlistAddTrack(trackObject, playlistList)
            }
        }

        recyclerView.adapter = playerPlayListAdapter
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.addButton.setOnClickListener {
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.state = STATE_COLLAPSED
            viewModel.getPlaylists()
        }
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {
                            binding.overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }
        )
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerActivity_to_createPlayListFragment)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.playListList.observe(viewLifecycleOwner) { it ->
            Log.d("playListList", "$it")
            if (it.isNullOrEmpty()) {

                return@observe
            } else {
                recyclerView.adapter = playerPlayListAdapter
                playerPlayListAdapter.setItems(it)
                return@observe
            }
        }
        if (track != null) {
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.durationName.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val dateObj = dateFormat.parse(track.releaseDate)
            val formattedDate = SimpleDateFormat("yyyy", Locale.getDefault()).format(dateObj)
            binding.yearName.text = formattedDate

            binding.albumName.text = track.collectionName
            binding.GenreName.text = track.primaryGenreName
            binding.countryName.text = track.country
            Glide.with(this)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_512)
                .error(R.drawable.placeholder_512)
                .transform(RoundedCorners(8))
                .into(binding.trackCover)
            if (!track.collectionName.isNullOrEmpty()) {
                binding.albumName.visibility = View.VISIBLE
                binding.album.visibility = View.VISIBLE
            }

            if (!track.primaryGenreName.isNullOrEmpty()) {
                binding.GenreName.visibility = View.VISIBLE
                binding.Genre.visibility = View.VISIBLE
            }



            track.previewUrl?.let {
                viewModel.preparePlayer(it) {
                    binding.playButton.isEnabled = true
                }
            }
        }
        binding.playButton.setOnClickListener {
            playbackControl()
        }

        viewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                binding.playButton.setImageResource(R.drawable.button_pause)
            } else {
                binding.playButton.setImageResource(R.drawable.button_play)
            }
        }

        if (trackObject != null) {
            viewModel.checkTrackInFavorite(trackObject)
                .observe(viewLifecycleOwner) { favourtitesIndicator ->
                    if (favourtitesIndicator) {
                        trackObject.isFavorite = true
                        binding.buttonLike.setImageResource(R.drawable.button_like_red)
                    } else {
                        binding.buttonLike.setImageResource(R.drawable.button_like)
                    }
                }
        }
        binding.buttonLike.setOnClickListener {
            if (trackObject != null) {
                viewModel.FavoriteTrack(trackObject)
            }
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
                } else if (playerState == 1) {
                    binding.playButton.setImageResource(R.drawable.button_play)
                    binding.progressOfTheWork.text = "00:00"
                }

                delay(300)
            }
        }
    }

    private fun playlistAddTrack(track: Track, playlist: Playlist) {
        var trackIsAdded = false
        viewModel.addTrack(track, playlist)
        lifecycleScope.launch {
            delay(300)
            viewModel.playListAdding.observe(viewLifecycleOwner) { playlistAdding ->
                val playlistName = playlist.playlistName
                if (!trackIsAdded) {
                    if (playlistAdding) {
                        val toastMessage = "Трек уже добавлен в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true

                        return@observe
                    } else {
                        val toastMessage = "Добавлено в плейлист $playlistName"
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT)
                            .show()
                        trackIsAdded = true
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        return@observe
                    }
                }
            }
        }
    }
}













