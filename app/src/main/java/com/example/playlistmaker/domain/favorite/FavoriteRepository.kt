package com.example.playlistmaker.domain.favorite

import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun addTrack(track:Track)
    fun deleteTrack(track: Track)
    fun getFavorite() : Flow<List<Track>>
    fun checkTreckInFavorite(id: Int): Flow<Boolean>
}