package com.example.playlistmaker.domain.playList

import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {
    fun createPlayList(playlistName: String,
                       description: String?,
                       uri: String)
    fun getPlayList() : Flow<List<Playlist>>
    fun update(track: Track, playlist: Playlist)
}