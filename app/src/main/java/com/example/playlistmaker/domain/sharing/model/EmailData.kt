package com.example.playlistmaker.domain.sharing.model

data class EmailData(
    val recipient: String,
    val subject: String,
    val body: String
)