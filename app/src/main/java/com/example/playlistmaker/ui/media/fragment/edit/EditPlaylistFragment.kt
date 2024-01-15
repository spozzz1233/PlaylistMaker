package com.example.playlistmaker.ui.mediaLibrary.fragments.playlist

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EditPlaylistBinding
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.ui.media.view_model.EditPlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tbruyelle.rxpermissions3.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditPlaylistFragment : Fragment() {
    private lateinit var binding: EditPlaylistBinding
    private lateinit var bottomNavigator: BottomNavigationView
    private val viewModel: EditPlaylistViewModel by viewModel()
    private var selectedUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditPlaylistBinding.inflate(inflater, container, false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavigator.visibility = View.GONE


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = arguments?.getParcelable<Playlist>("playlist")
        val name = binding.playlistNameEditText


        if (playlist != null) {
            playlist.playlistId?.let { viewModel.getUpdatePlayListById(it) }
        }
        viewModel.updatedPlaylist.observe(viewLifecycleOwner) {
            name.setText(it.playlistName)
            binding.playlistDescriptEditText.setText(it.description)
            val image = (it?.uri ?: "Unknown Cover")

            if (image != "Unknown Cover") {
                binding.playlistPlaceHolder.visibility = View.GONE
                Glide.with(this)
                    .load(image)
                    .centerCrop()
                    .transform(CenterCrop())
                    .placeholder(R.drawable.placeholder)
                    .override(312, 312)
                    .into(binding.playlistCover)
                selectedUri = image.toUri()
            }
        }

        val rxPermissions = RxPermissions(this)

        binding.back.setOnClickListener {
            closer()
        }


        turnOnCreateButton()
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    turnOffCreateButton()
                } else {
                    turnOnCreateButton()
                }
            }
        }
        binding.playlistNameEditText.addTextChangedListener(simpleTextWatcher)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val radius = 8
                    val width = 312
                    val height = 312
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_photo)
                        .transform(CenterCrop(), RoundedCorners(radius))
                        .override(width, height)
                        .into(binding.playlistCover)
                    saveImageToPrivateStorage(uri)

                } else {
                    //ничего не делаем
                }
            }

        binding.playlistCover.setOnClickListener {
            rxPermissions.request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    } else {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            closer()
        }
        binding.saveButton.setOnClickListener {
            if (binding.playlistNameEditText.text.toString()
                    .isEmpty()
            ) return@setOnClickListener
            if (playlist != null) {
                savePlaylist(playlist)
            }
        }


    }

    private fun closer() {
        findNavController().popBackStack()
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileCount = filePath.listFiles()?.size ?: 0
        val file = File(filePath, "first_cover_${fileCount + 1}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        binding.playlistPlaceHolder.visibility = View.GONE
        selectedUri = file.toUri()
    }

    private fun turnOffCreateButton() {
        binding.saveButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.light_grey))
        binding.saveButton.isEnabled = false
    }

    private fun turnOnCreateButton() {
        binding.saveButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.YP_Blue))
        binding.saveButton.isEnabled = true
    }

    private fun savePlaylist(playlist: Playlist) {
        viewModel.savePlayList(
            playlist,
            binding.playlistNameEditText.text.toString(),
            binding.playlistDescriptEditText.text.toString(),
            selectedUri.toString(),
        )
        closer()
    }
}