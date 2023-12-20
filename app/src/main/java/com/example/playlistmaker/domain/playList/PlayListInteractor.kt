package com.example.playlistmaker.domain.playList

import com.example.playlistmaker.domain.playList.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {
    fun createPlayList(playlistName: String,
                       description: String?,
                       uri: String)
    fun getPlayList() : Flow<List<Playlist>>
}