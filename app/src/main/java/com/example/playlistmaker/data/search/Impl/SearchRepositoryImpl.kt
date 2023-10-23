package com.example.playlistmaker.data.search.Impl



import com.example.playlistmaker.data.api.SearchApi
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.search.model.tracks
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.Resource
import com.example.playlistmaker.data.search.network.TrackSearchRequest
import com.example.playlistmaker.domain.search.ErrorType
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

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
                            track.trackName,
                            track.artistName,
                            track.trackTimeMillis,
                            track.artworkUrl100,
                            track.collectionName,
                            track.releaseDate,
                            track.primaryGenreName,
                            track.country,
                            track.previewUrl
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
