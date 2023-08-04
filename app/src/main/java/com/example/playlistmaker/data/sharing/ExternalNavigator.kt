package com.example.playlistmaker.data.sharing

import android.content.Intent

interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
    fun setShareLink(): String
}