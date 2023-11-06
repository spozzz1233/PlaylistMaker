package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.network.Resource
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val searchRepository: SearchRepository): SearchInteractor {
    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> {
        return searchRepository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    (Resource.Success(result.data))
                }
                is Resource.Error<*> -> {
                    Resource.Error(result.message, null)
                }
            } as Resource<List<Track>>
        }
    }
}

