package com.example.playlistmaker.domain.callback

import com.example.playlistmaker.domain.model.Track

interface SearchCallback {
    fun recyclerViewSearchGone()
    fun recyclerViewSearchVisible()
    fun progressBarGone()
    fun progressBarVisible()
    fun noResultVisible()
    fun historyVisible()
    fun historyGone()
    fun noInternetVisible()
}