package com.example.playlistmaker.domain.playList

import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val playListRepository: PlayListRepository): PlayListInteractor {
    override fun createPlayList(playlistName: String, description: String?, uri: String) {
        playListRepository.createPlayList(playlistName,playlistName,uri)
    }

    override fun getPlayList(): Flow<List<Playlist>> {
        return playListRepository.getPlayList()
    }
    override fun update(track: Track, playlist: Playlist) {
        playListRepository.update(track, playlist)
    }

}