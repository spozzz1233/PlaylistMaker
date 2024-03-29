package com.example.playlistmaker.data.favorite

import com.example.playlistmaker.data.converters.TrackConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.favorite.FavoriteRepository
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val TrackConverter: TrackConverter,
) : FavoriteRepository {


    override fun addTrack(track: Track) {
        track.isFavorite = true
        appDatabase.TrackDao().insertTrack(track)
    }

    override fun deleteTrack(track: Track) {
        track.isFavorite = false
        TrackConverter.mapTrackToFavourite(track).let {  appDatabase.TrackDao().deleteTrack(it) }
    }

    override fun getFavorite(): Flow<List<Track>>{
        return appDatabase.TrackDao().getFavoriteTracks()
            .map { trackEntityList ->
                trackEntityList.map { TrackConverter.mapFavouriteToTrack(it) }
            }

    }
    override fun checkTreckInFavorite(id: Int): Flow<Boolean> = flow {
        emit(appDatabase.TrackDao().checkTreckInFavorite(id) != null)
    }
}