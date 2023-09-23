package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.media.view_model.FragmentPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylist : Fragment() {
    private val vm by viewModel<FragmentPlaylistViewModel>()
    companion object {
        fun newInstance() = FragmentPlaylist()

    }

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
}

