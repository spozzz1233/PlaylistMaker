package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository): FavoriteInteractor {
    override fun addTrack(track: Track) {
        favoriteRepository.addTrack(track)
    }

    override fun deleteTrack(track: Track) {
        favoriteRepository.deleteTrack(track)
    }

    override fun getFavorite(): Flow<List<Track>> {
        return favoriteRepository.getFavorite()
    }

    override fun checkTreckInFavorite(id: Int): Flow<Boolean> {
        return favoriteRepository.checkTreckInFavorite(id)
    }
}