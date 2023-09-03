package com.example.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentMediaLibraryBinding
import java.text.FieldPosition

class FragmentMediaLibrary : Fragment(){
    companion object {
        private const val TEXT = "text"
        private const val POSITION = "position"

        fun newInstance(text: String, position: Int) = FragmentMediaLibrary().apply {
            arguments = Bundle().apply {
                putString(TEXT, text)
                putInt(POSITION, position)
            }
        }
    }

    private lateinit var binding: FragmentMediaLibraryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = requireArguments().getString(TEXT)
        val position = requireArguments().getInt(POSITION)

        binding.text.text = text

        // Устанавливаем видимость newPlaylist в зависимости от позиции
        when (position) {
            0 -> binding.newPlaylist.visibility = View.GONE
            1 -> binding.newPlaylist.visibility = View.VISIBLE
        }
    }
}