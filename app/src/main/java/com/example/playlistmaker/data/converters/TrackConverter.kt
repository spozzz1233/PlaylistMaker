package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.PlayListTracks.PlayListTracksEntity
import com.example.playlistmaker.data.favorite.entity.TrackEntity
import com.example.playlistmaker.domain.search.model.Track

class TrackConverter {
    fun mapTrackToFavourite(track: Track): TrackEntity {
        return TrackEntity(
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
            track.isFavorite,
            track.addedTimestamp
        )
    }

    fun mapFavouriteToTrack(track: TrackEntity): Track {
        return Track(
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
            track.isFavorite,
            track.addedTimestamp
        )
    }
    fun mapTrackEntityToTrack(entity: PlayListTracksEntity): Track {
        return Track(
            entity.trackId.toInt(),
            entity.trackName,
            entity.artistName,
            entity.trackTimeMillis,
            entity.artworkUrl100,
            entity.collectionName,
            entity.releaseDate,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl,
            entity.isFavorite,
            entity.addTime
        )
    }
}