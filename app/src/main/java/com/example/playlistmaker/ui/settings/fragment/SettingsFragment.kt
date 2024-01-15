package com.example.playlistmaker.ui.settings.fragment

import SettingsFragmentViewModel
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val vm by viewModel<SettingsFragmentViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.share.setOnClickListener {
            vm.share()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            vm.updateThemeSettings(isChecked)
        }

        binding.Support.setOnClickListener {
            vm.support()
        }

        binding.terms.setOnClickListener {
            vm.terms()
        }


        vm.themeSettingsLiveData.observe(viewLifecycleOwner, { themeSettings ->
            binding.themeSwitcher.isChecked = when (themeSettings) {
                is ThemeSettings.LightTheme -> false
                is ThemeSettings.DarkTheme -> true
            }
            if (binding.themeSwitcher.isChecked) {
                binding.themeSwitcher.thumbTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.YP_Blue))
                binding.themeSwitcher.trackTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.YP_Blue_Light))
            }
        })
    }


}