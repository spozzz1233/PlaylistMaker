package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun addTrack(track: Track)
    fun deleteTrack(track: Track)
    fun getFavorite(): Flow<List<Track>>
    fun checkTreckInFavorite(id: Int): Flow<Boolean>
}