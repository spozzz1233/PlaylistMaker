package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData


class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {
    override fun shareLink(url: String) {
        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, url)
        context.startActivity(intentSend)
    }

    override fun openLink(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW,Uri.parse(url))
        context.startActivity(browserIntent)
    }

    override fun openEmail(body: EmailData) {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(body.recipient))
        shareIntent.putExtra(Intent.EXTRA_TITLE, body.body)
        shareIntent.putExtra(Intent.EXTRA_TEXT, body.subject)
        context.startActivity(shareIntent)
    }

}