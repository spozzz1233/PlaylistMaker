package com.example.playlistmaker.data.search.Impl

import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.Resource
import com.example.playlistmaker.data.search.network.TrackSearchRequest
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(private val networkClient: NetworkClient): SearchRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        try{
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            when (response.resultCode) {
                -1 -> {
                    emit(Resource.Error(ErrorType.CONNECTION_ERROR))
                }
                200 -> {
                    emit(Resource.Success((response as TracksResponse).results.map {track ->
                        Track(
                            track.trackId,
                            track.trackName,
                            track.artistName,
                            track.trackTimeMillis,
                            track.artworkUrl100,
                            track.collectionName,
                            track.releaseDate,
                            track.primaryGenreName,
                            track.country,
                            track.previewUrl,
                            addedTimestamp = System.currentTimeMillis()
                        )
                    }))
                }
                else -> {
                    emit(Resource.Error(ErrorType.SERVER_ERROR))
                }
            }
        }catch (error:Error){
            throw Exception(error)
        }

    }

}
