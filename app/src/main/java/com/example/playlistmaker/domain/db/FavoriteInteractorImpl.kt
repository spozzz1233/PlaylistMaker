package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository): FavoriteInteractor {
    override fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }

    override fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }
}