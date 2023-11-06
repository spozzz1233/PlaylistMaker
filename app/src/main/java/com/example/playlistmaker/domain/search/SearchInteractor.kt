package com.example.playlistmaker.domain.search

import com.example.playlistmaker.data.search.network.Resource
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTrack (expression:String) : Flow<Resource<List<Track>>>
}
