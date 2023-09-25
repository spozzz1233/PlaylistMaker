package com.example.playlistmaker.ui.media.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediatekaBinding
import com.example.playlistmaker.ui.media.adapter.MediaViewPagerAdapter
import com.example.playlistmaker.ui.media.view_model.FragmentPlaylistViewModel
import com.example.playlistmaker.ui.media.view_model.MediatekaViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediatekaActivity : AppCompatActivity() {
    private val vm by viewModel<MediatekaViewModel>()

    private lateinit var binding: ActivityMediatekaBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.selectedTracks)
                1 -> tab.text = resources.getString(R.string.playlist)
            }
        }
        tabMediator.attach()
        binding.back.setOnClickListener {
            finish()
        }
    }

}