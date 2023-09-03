package com.example.playlistmaker.ui.media.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.activity.FragmentMediaLibrary

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
//        return FragmentMediaLibrary()
        return when(position) {
            0 -> FragmentMediaLibrary.newInstance("Ваша медиатека пуста",0)
            else -> {FragmentMediaLibrary.newInstance("Вы не создали \n ни одного плейлиста",1)

            }
        }
    }
}