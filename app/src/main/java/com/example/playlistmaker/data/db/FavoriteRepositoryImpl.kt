package com.example.playlistmaker.data.db

import com.example.playlistmaker.data.converters.TrackConverter
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.search.model.Track

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


}