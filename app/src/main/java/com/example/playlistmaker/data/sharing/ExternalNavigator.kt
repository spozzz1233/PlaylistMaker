package com.example.playlistmaker.data.sharing

import android.content.Intent
import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(url: String)
    fun openLink(url: String)
    fun openEmail(body: EmailData)

}