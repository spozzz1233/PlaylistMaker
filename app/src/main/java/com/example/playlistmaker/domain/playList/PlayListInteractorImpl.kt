package com.example.playlistmaker.domain.playList

import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(private val playListRepository: PlayListRepository): PlayListInteractor {
    override fun createPlayList(playlistName: String, description: String?, uri: String) {
        playListRepository.createPlayList(playlistName,description,uri)
    }

    override fun getPlayList(): Flow<List<Playlist>> {
        return playListRepository.getPlayList()
    }
    override fun update(track: Track, playlist: Playlist) {
        playListRepository.update(track, playlist)
    }

    override fun getUpdatePlayListById(id: Int): Flow<Playlist> {
        return playListRepository.getUpdatePlayListById(id)
    }

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> {
        return playListRepository.getTrackList(playlist)
    }

    override fun deletePlaylist(playlist: Playlist) {
        playListRepository.deletePlaylist(playlist)
    }
    override fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        playListRepository.savePlaylist(
            playlist,
            playlistName,
            description,
            uri
        )
    }

    override fun durationCounting(playlist: Playlist): Flow<String> {
        return playListRepository.durationCounting(playlist)
    }

    override fun deleteIfIsNotInPlaylist(searchId: Long) {
        playListRepository.deleteIfIsNotInPlaylist(searchId)
    }

    override fun getTrackId(): Flow<List<Long>> {
        return playListRepository.getTrackId()
    }


}