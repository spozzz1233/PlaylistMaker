package com.example.playlistmaker.ui.media.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.activity.FragmentFavorite
import com.example.playlistmaker.ui.media.activity.FragmentPlaylist

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        val itemCount = 2
        Log.d("MediaViewPagerAdapter", "getItemCount: $itemCount")
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {

        return if (position == 0) {
            FragmentFavorite.newInstance()
        } else {
            FragmentPlaylist.newInstance()
        }
    }

}