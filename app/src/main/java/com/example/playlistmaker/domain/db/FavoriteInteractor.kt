package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track

interface FavoriteInteractor {
    fun addTrack(track: Track)
    fun deleteTrack(track: Track)
}