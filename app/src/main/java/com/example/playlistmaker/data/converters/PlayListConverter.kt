package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.playlist.entity.PlayListEntity
import com.example.playlistmaker.domain.playList.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlayListConverter {
    val gson = Gson()

    fun mapplaylistEntityToClass(item: PlayListEntity): Playlist {

        return Playlist(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.fromJson(item.trackList, object : TypeToken<List<Long>>() {}.type),
            item.arrayNumber
        )
    }

    fun mapplaylistClassToEntity(item: Playlist): PlayListEntity {
        return PlayListEntity(
            item.playlistId,
            item.playlistName,
            item.description,
            item.uri,
            gson.toJson(item.trackArray),
            item.arrayNumber
        )
    }
}