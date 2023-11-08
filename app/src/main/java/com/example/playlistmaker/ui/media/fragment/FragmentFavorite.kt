package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.ui.media.adapter.FragmentFavoriteAdapter
import com.example.playlistmaker.ui.media.view_model.FragmentFavoriteViewModel
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentFavorite : Fragment(){
    private val viewModel by viewModel<FragmentFavoriteViewModel>()
    private lateinit var FragmentFavoriteAdapter: FragmentFavoriteAdapter

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initial()
        lifecycleScope.launchWhenStarted {
            viewModel.getFavoriteTracks().collect { favoriteTracks ->
                FragmentFavoriteAdapter.setItems(favoriteTracks)
            }
        }
    }



    private fun initial() {
        val recyclerViewSearch = binding.recyclerViewFavorite
        FragmentFavoriteAdapter = FragmentFavoriteAdapter{ track->
            if (clickDebounce()) {
                findNavController().navigate(
                    R.id.action_mediatekaFragment_to_playerActivity,
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
        }
        recyclerViewSearch.adapter = FragmentFavoriteAdapter
    }

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(FragmentFavorite.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    companion object {
        fun newInstance() = FragmentFavorite()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}