package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.media.adapter.FragmentPlayListAdapter
import com.example.playlistmaker.ui.media.view_model.FragmentPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylist : Fragment() {
    private val vm by viewModel<FragmentPlaylistViewModel>()
    private lateinit var fragmentPlayListAdapter: FragmentPlayListAdapter
    companion object {
        fun newInstance() = FragmentPlaylist()

    }

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylist.setOnClickListener{
            findNavController().navigate(R.id.createPlayListFragment)
        }
        val recyclerView = binding.recyclerViewPlayList
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        fragmentPlayListAdapter = FragmentPlayListAdapter()
        recyclerView.adapter = fragmentPlayListAdapter


        vm.getPlaylists()

        vm.playListList.observe(viewLifecycleOwner) { it ->
            Log.d("playListList","$it")
            if (it.isNullOrEmpty()) {

                return@observe
            } else {
                recyclerView.adapter = fragmentPlayListAdapter
                fragmentPlayListAdapter.setItems(it)
                return@observe
            }
        }
    }
}

