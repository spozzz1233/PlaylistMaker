package com.example.playlistmaker.ui.playlist.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentScreenPlaylistBinding
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.playlist.view_model.PlayListViewModel
import com.example.playlistmaker.ui.search.adapters.HistoryAdapter
import com.example.playlistmaker.ui.search.adapters.searchAdapter
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScreenPlaylistFragment : Fragment() {
    lateinit var binding: FragmentScreenPlaylistBinding
    private val viewModel by viewModel<PlayListViewModel>()
    private lateinit var adapter: searchAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScreenPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = arguments?.getParcelable<Playlist>("playlist")
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.share.setOnClickListener {
            if (playlist != null) {
                sharePlaylist(playlist)
            }
        }
        if (playlist != null) {
            playlist.playlistId?.let{viewModel.getUpdatePlayListById(it)}

        }
        playlist?.let {
            var updatePlayList = it
            viewModel.updatedPlaylist.observe(viewLifecycleOwner){playlist ->
                updatePlayList = playlist
                binding.playListName.text = updatePlayList.playlistName
                binding.description.text = updatePlayList.description?: ""
                val getImage = updatePlayList.uri
                if (getImage != "null") {
                    Log.d("картинка", getImage)
                    Glide.with(this)
                        .load(getImage)
                        .centerCrop()
                        .transform(CenterCrop(),RoundedCorners(6))
                        .override(312, 312)
                        .placeholder(R.drawable.placeholder_512)
                        .into(binding.playlistCover)
                }
                val numberOfTracks = updatePlayList.arrayNumber.toString()
                val arrayNumber = when{
                    numberOfTracks.toInt() % 10 == 1 && numberOfTracks.toInt() % 100 != 11 -> " трек"
                    numberOfTracks.toInt() % 10 == 2 && numberOfTracks.toInt() % 100 != 12 -> " трека"
                    numberOfTracks.toInt() % 10 == 3 && numberOfTracks.toInt() % 100 != 13 -> " трека"
                    numberOfTracks.toInt() % 10 == 4 && numberOfTracks.toInt() % 100 != 14 -> " трека"
                    else -> " треков"
                }
                binding.numberOfTracks.text = "$numberOfTracks $arrayNumber"
                initial(updatePlayList)
            }
        }


        binding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.more.setOnClickListener{
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            playlist?.let {
                binding.namePlaylistMenu.text = it.playlistName
                val numberOfTracks= it.arrayNumber.toString()
                val arrayNumber = when{
                    numberOfTracks.toInt() % 10 == 1 && numberOfTracks.toInt() % 100 != 11 -> " трек"
                    numberOfTracks.toInt() % 10 == 2 && numberOfTracks.toInt() % 100 != 12 -> " трека"
                    numberOfTracks.toInt() % 10 == 3 && numberOfTracks.toInt() % 100 != 13 -> " трека"
                    numberOfTracks.toInt() % 10 == 4 && numberOfTracks.toInt() % 100 != 14 -> " трека"
                    else -> " треков"
                }
                binding.numberOfTracksMenu.text = "$numberOfTracks $arrayNumber"
                val getImage = it.uri
                if (getImage != "null") {
                    Glide.with(this)
                        .load(getImage)
                        .centerCrop()
                        .transform(CenterCrop(),RoundedCorners(2))
                        .override(45, 45)
                        .placeholder(R.drawable.placeholder_512)
                        .into(binding.imageMenu)
                }
            }
        }
        binding.shareMenu.setOnClickListener {
            if (playlist != null) {
                sharePlaylist(playlist)
            }
        }
        binding.editInformationMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            findNavController().navigate(R.id.action_screenPlaylistFragment_to_editPlaylistFragment, bundle)
        }
        binding.deletePlaylistMenu.setOnClickListener {
            playlist?.let {


                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Хотите удалить плейлист «${it.playlistName}»?") // Заголовок диалога
                    .setNegativeButton("НЕТ") { dialog, which -> // Добавляет кнопку «Отмена»

                    }
                    .setPositiveButton("ДА") { dialog, which -> // Добавляет кнопку «Нет»
                        viewModel.deletePlayList(it)
                        finish()
                    }
                    .show()
            }

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
    }
    private fun initial(playlist: Playlist) {
        val recyclerViewSearch = binding.recyclerView
        adapter = searchAdapter(
            clickListener = { track ->
                if (clickDebounce()) {
                    findNavController().navigate(R.id.action_screenPlaylistFragment_to_playerActivity,
                        PlayerActivity.createArgs(
                            track.trackId,
                            track.trackName?: "",
                            track.previewUrl?: "",
                            track.artistName?: "",
                            track.trackTimeMillis?: 0,
                            track.artworkUrl100?: "",
                            track.collectionName?: "",
                            track.releaseDate?: "",
                            track.primaryGenreName?: "",
                            track.country?: ""
                        )
                    )

                }
            },
            longClickListener = {track ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Хотите удалить трек?")
                    .setNegativeButton("НЕТ") { dialog, which ->

                    }
                    .setPositiveButton("ДА") { dialog, which ->
                        deleteTrackByClick(track,playlist)
                    }
                    .show()
            }
        )
        viewModel.getTrackList(playlist)
        viewModel.trackList.observe(viewLifecycleOwner){
            trackList ->
            val arrayListTrackList = ArrayList(trackList)
            adapter.newTracks(arrayListTrackList)
            adapter.updateData()
        }
        recyclerViewSearch.adapter = adapter
    }
    private fun sharePlaylist(playlist: Playlist) {
        val nameOfPlaylist = playlist.playlistName
        val desriptionOfPlaylist = playlist.description
        val trackNumber = playlist.arrayNumber
        if (trackNumber == 0) {
            val message = "В данном плейлисте нет списка треков, которым можно поделиться."
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            return
        }
        var trackInfo = "$nameOfPlaylist \n $desriptionOfPlaylist \n $trackNumber треков \n"
        val trackList: List<Track> = viewModel.trackList.value!!
        var i = 0
        trackList.forEach { track ->
            i += 1
            val name = track.trackName
            val duration = track.trackTimeMillis
            trackInfo = "$trackInfo $i. $name  - ($duration) \n"
        }

        val intentSend = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, trackInfo)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            Intent.createChooser(this, null)
        }
        requireContext().startActivity(intentSend, null)
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteTrackByClick(item: Track, playlist: Playlist) {
        viewModel.deleteTrack(item, playlist)
        viewModel.getTrackList(playlist)
        viewModel.deletedTrack.observe(viewLifecycleOwner) { isTrackDeleted ->
            if (isTrackDeleted) {
                playlist.playlistId?.let { viewModel.getUpdatePlayListById(it) }
                viewModel.getTrackList(playlist)
            }
        }

    }
    fun finish(){
        if (isAdded) {  // Проверяем, привязан ли фрагмент к активности
            findNavController().popBackStack()
        }
    }
    private var isClickAllowed = true
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}