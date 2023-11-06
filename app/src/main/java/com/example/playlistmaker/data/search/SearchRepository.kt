package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.network.Resource
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow



interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}