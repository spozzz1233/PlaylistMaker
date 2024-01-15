package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlaylist : Fragment() {
    private val vm by viewModel<FragmentPlaylistViewModel>()
    private lateinit var fragmentPlayListAdapter: FragmentPlayListAdapter
    private lateinit var bottomNavigator: BottomNavigationView

    private lateinit var binding: FragmentPlaylistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.getPlaylists()
        binding.newPlaylist.setOnClickListener{
            findNavController().navigate(R.id.createPlayListFragment)
            bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
            bottomNavigator.visibility = View.GONE
        }
        val recyclerView = binding.recyclerViewPlayList
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        fragmentPlayListAdapter = FragmentPlayListAdapter{
            val bundle = Bundle()
            bundle.putParcelable("playlist", it)
            findNavController().navigate(R.id.action_mediatekaFragment_to_screenPlaylistFragment,bundle)
        }
        recyclerView.adapter = fragmentPlayListAdapter
        fragmentPlayListAdapter.updateData()
        vm.playListList.observe(viewLifecycleOwner) { it ->
            if (it.isNullOrEmpty()) {
                binding.recyclerViewPlayList.visibility = View.GONE
                binding.placeholderImage.visibility = View.VISIBLE
                binding.placeholderText.visibility = View.VISIBLE
                return@observe
            } else {
                binding.recyclerViewPlayList.visibility = View.VISIBLE
                binding.placeholderImage.visibility = View.GONE
                binding.placeholderText.visibility = View.GONE
                recyclerView.adapter = fragmentPlayListAdapter
                fragmentPlayListAdapter.setItems(it)
                return@observe
            }
        }
    }

    companion object {
        fun newInstance() = FragmentPlaylist()

    }
}

