package com.example.practicum.playlist.domain.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData



class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return "https://practicum.yandex.ru/profile/android-developer"
    }

    private fun getSupportEmailData(): EmailData {
        val body = "234"//context.getString(R.string.header)
        val subject = "234"//context.getString(R.string.mesage_support)
        val recipient = "minulin.misha@bk.ru"
        return EmailData(recipient, subject, body)
    }

    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
    }
}