package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun addTrack(track:Track)
    fun deleteTrack(track: Track)
    fun getFavorite() : Flow<List<Track>>
    fun checkTreckInFavorite(id: Int): Flow<Boolean>
}