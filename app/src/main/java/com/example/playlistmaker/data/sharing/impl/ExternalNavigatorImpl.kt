package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.sharing.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {
    override fun shareLink() {
        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, setShareLink ())
        context.startActivity(intentSend)
    }

    override fun openLink() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
        context.startActivity(browserIntent)
    }

    override fun openEmail() {
//        val message = getString(R.string.share)
//        val shareIntent = Intent(Intent.ACTION_SENDTO)
//        shareIntent.data = Uri.parse("mailto:")
//        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail)))
//        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
//        startActivity(shareIntent)
    }

    override fun setShareLink(): String {
        return "https://practicum.yandex.ru/profile/android-developer"
    }
}