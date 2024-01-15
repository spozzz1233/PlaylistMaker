package com.example.playlistmaker.ui.playlist.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentScreenPlaylistBinding
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.ui.playlist.view_model.PlayListViewModel
import com.example.playlistmaker.ui.search.adapters.searchAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

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
        val bottomNavigationView: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.share.setOnClickListener {
            viewModel.updatedPlaylist.observe(viewLifecycleOwner) { playlist ->
                sharePlaylist(playlist)
            }
        }
        if (playlist != null) {
            playlist.playlistId?.let { viewModel.getUpdatePlayListById(it) }
        }

        playlist?.let {
            var updatePlayList = it
            viewModel.updatedPlaylist.observe(viewLifecycleOwner) { playlist ->
                updatePlayList = playlist
                binding.playListName.text = updatePlayList.playlistName
                binding.description.text = updatePlayList.description ?: ""
                playlistTime(updatePlayList)
                if (updatePlayList.arrayNumber == 0) {
                    binding.placeholder.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.placeholder.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE

                }
                val image = updatePlayList.uri
                loadImage(image)
                val numberOfTracks = updatePlayList.arrayNumber
                binding.numberOfTracks.text = numberOfTracks?.let { it1 -> formatArrayNumber(it1) }
                initial(updatePlayList)
            }
        }


        binding.back.setOnClickListener {
            finish()
        }
        binding.more.setOnClickListener {
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            playlist?.let {
                var updatePlayList = it
                viewModel.updatedPlaylist.observe(viewLifecycleOwner) { playlist ->
                    updatePlayList = playlist
                    binding.namePlaylistMenu.text = updatePlayList.playlistName
                    val numberOfTracks = updatePlayList.arrayNumber
                    binding.numberOfTracksMenu.text = numberOfTracks?.let { it1 ->
                        formatArrayNumber(
                            it1
                        )
                    }
                    val image = updatePlayList.uri
                    loadImage(image)
                }
            }
        }
        binding.shareMenu.setOnClickListener {
            viewModel.updatedPlaylist.observe(viewLifecycleOwner) { playlist ->
                sharePlaylist(playlist)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

        }
        binding.editInformationMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("playlist", playlist)
            findNavController().navigate(
                R.id.action_screenPlaylistFragment_to_editPlaylistFragment,
                bundle
            )
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.deletePlaylistMenu.setOnClickListener {
            playlist?.let {


                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Хотите удалить плейлист «${it.playlistName}»?") // Заголовок диалога
                    .setNegativeButton("НЕТ") { dialog, which -> // Добавляет кнопку «Отмена»

                    }
                    .setPositiveButton("ДА") { dialog, which -> // Добавляет кнопку «Нет»
                        viewModel.deletePlayList(it)
                        finish()
                    }
                    .show()
                val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)

                positiveButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.YP_Blue
                    )
                )
                negativeButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.YP_Blue
                    )
                )
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
                val bundle = Bundle()
                bundle.putParcelable("track", track)
                findNavController().navigate(
                    R.id.action_screenPlaylistFragment_to_playerActivity, bundle
                )
            },
            longClickListener = { track ->
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Хотите удалить трек?")
                    .setNegativeButton("НЕТ") { dialog, which ->

                    }
                    .setPositiveButton("ДА") { dialog, which ->
                        deleteTrackByClick(track, playlist)
                    }
                    .show()
                val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)

                positiveButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.YP_Blue
                    )
                )
                negativeButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.YP_Blue
                    )
                )
            }
        )
        viewModel.getTrackList(playlist)
        viewModel.trackList.observe(viewLifecycleOwner) { trackList ->
            val arrayListTrackList = ArrayList(trackList.reversed())
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
        var trackInfo = "$nameOfPlaylist \n $desriptionOfPlaylist \n ${
            playlist.arrayNumber?.let {
                formatArrayNumber(
                    it
                )
            }
        } \n"
        val trackList: List<Track> = viewModel.trackList.value!!
        var i = 0
        trackList.forEach { track ->
            i += 1
            val name = track.trackName
            val artistName = track.artistName
            val duration = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            trackInfo = "$trackInfo $i. $artistName - $name ($duration) \n"
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

    private fun playlistTime(playlist: Playlist) {
        viewModel.getPlaylistTime(playlist)
        viewModel.playlistTime.observe(viewLifecycleOwner) { playlistTime ->
            binding.duration.text = playlistTime
        }
    }

    private fun formatArrayNumber(numberOfTracks: Int): String {
        val arrayNumber = when {
            numberOfTracks % 10 == 1 && numberOfTracks % 100 != 11 -> " трек"
            numberOfTracks % 10 == 2 && numberOfTracks % 100 != 12 -> " трека"
            numberOfTracks % 10 == 3 && numberOfTracks % 100 != 13 -> " трека"
            numberOfTracks % 10 == 4 && numberOfTracks % 100 != 14 -> " трека"
            else -> " треков"
        }
        return "$numberOfTracks$arrayNumber"
    }

    private fun loadImage(imageUrl: String) {
        if (imageUrl != "null") {
            Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .transform(CenterCrop(), RoundedCorners(6))
                .placeholder(R.drawable.placeholder_512)
                .into(binding.playlistCover)
        }
    }

    fun finish() {

        findNavController().popBackStack()

    }
}