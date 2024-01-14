package com.example.playlistmaker.ui.media.fragment.edit

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlayListBinding
import com.example.playlistmaker.ui.media.view_model.FragmentCreatePlayListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions3.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlayListFragment : Fragment() {
    private val viewModel by viewModel<FragmentCreatePlayListViewModel>()
    private lateinit var binding: FragmentCreatePlayListBinding
    private var Uri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = FragmentCreatePlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rxPermissions = RxPermissions(this)
        binding.back.setOnClickListener {
            backClick()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                turnOff()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                turnOn()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    turnOff()
                } else {
                    turnOn()
                }
            }
        }
        binding.createButton.setOnClickListener {
            val dialogPlaylistName = binding.playlistNameEditText.text
            createPlayList()
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setMessage("Плейлист $dialogPlaylistName создан")
                .setNegativeButton("Оk") { dialog, which ->
                    finish()
                }
                .show()
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)

            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
            finish()
        }
        binding.playlistNameEditText.addTextChangedListener(simpleTextWatcher)
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    Glide.with(requireActivity())
                        .load(uri)
                        .centerCrop()
                        .placeholder(R.drawable.add_photo)
                        .transform(CenterCrop(), RoundedCorners(8))
                        .override(312, 312)
                        .into(binding.playlistCover)
                    saveImageToPrivateStorage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.playlistCover.setOnClickListener {
            rxPermissions
                .request(android.Manifest.permission.READ_MEDIA_IMAGES)
                .subscribe { granted: Boolean ->
                    if (granted) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backClick()
        }

    }

    fun backClick() {
        val name = binding.playlistNameEditText.text
        val description = binding.playlistDescriptEditText.text
        if (!(name.isNullOrEmpty()) || !(description.isNullOrEmpty())) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?") // Заголовок диалога
                .setMessage("Все несохраненные данные будут потеряны") // Описание диалога
                .setNegativeButton("Отмена") { dialog, which -> // Добавляет кнопку «Отмена»

                }
                .setPositiveButton("Завершить") { dialog, which -> // Добавляет кнопку «Нет»
                    requireActivity().finish()
                }
                .show()
        } else {
            finish()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val fileCount = filePath.listFiles()?.size ?: 0
        val file = File(filePath, "first_cover${fileCount + 1}.jpg")
        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        binding.playlistPlaceHolder.visibility = View.GONE
        Uri = file.toUri()
    }

    fun finish() {
        if (isAdded) {
            findNavController().popBackStack()
        }
    }

    fun turnOn() {
        binding.createButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.YP_Blue))
        binding.createButton.isEnabled = true
    }

    fun turnOff() {
        binding.createButton.backgroundTintList =
            (ContextCompat.getColorStateList(requireContext(), R.color.light_grey))
        binding.createButton.isEnabled = false
    }

    fun createPlayList() {
        val name = binding.playlistNameEditText.text.toString()
        val description = binding.playlistDescriptEditText.text.toString()
        viewModel.createPlayList(name, description, Uri.toString())
    }

}