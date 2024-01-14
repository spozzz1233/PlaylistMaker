package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
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
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentFavorite : Fragment() {
    private val viewModel by viewModel<FragmentFavoriteViewModel>()
    private lateinit var FragmentFavoriteAdapter: FragmentFavoriteAdapter

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.isFavoriteEmpty.observe(viewLifecycleOwner, { favorite ->
            if (favorite) {
                binding.placeholder.isVisible = true
                binding.placeholderText.isVisible = true

            } else {
                binding.placeholder.isVisible = false
                binding.placeholderText.isVisible = false
            }
        })
    }


    private fun initial() {
        val recyclerViewSearch = binding.recyclerViewFavorite
        FragmentFavoriteAdapter = FragmentFavoriteAdapter { track ->
            val bundle = Bundle()
            bundle.putParcelable("track", track)
            findNavController().navigate(
                R.id.action_mediatekaFragment_to_playerActivity, bundle
            )
        }
        recyclerViewSearch.adapter = FragmentFavoriteAdapter
    }



    companion object {
        fun newInstance() = FragmentFavorite()
    }
}