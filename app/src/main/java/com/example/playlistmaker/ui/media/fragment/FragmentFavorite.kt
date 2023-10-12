package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.ui.media.view_model.FragmentFavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentFavorite : Fragment(){
    private val vm by viewModel<FragmentFavoriteViewModel>()
    companion object {



        fun newInstance() = FragmentFavorite()

    }

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

}