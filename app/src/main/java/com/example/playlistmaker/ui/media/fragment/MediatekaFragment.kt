package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.ui.media.adapter.MediaViewPagerAdapter
import com.example.playlistmaker.ui.media.view_model.MediatekaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class MediatekaFragment : Fragment() {

    private val vm by viewModel<MediatekaViewModel>()

    private lateinit var binding: FragmentMediatekaBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediatekaBinding.inflate(layoutInflater)

        binding.viewPager.adapter = MediaViewPagerAdapter(parentFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.selectedTracks)
                1 -> tab.text = resources.getString(R.string.playlist)
            }
        }
        tabMediator.attach()

        return binding.root
    }


}