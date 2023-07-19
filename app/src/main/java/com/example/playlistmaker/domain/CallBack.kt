package com.example.playlistmaker.domain

interface CallBack {
    fun recyclerViewSearchGone()
    fun recyclerViewSearchVisible()
    fun progressBarGone()
    fun progressBarVisible()
    fun noResultVisible()
    fun historyVisible()
    fun historyGone()
    fun noInternetVisible()

}
