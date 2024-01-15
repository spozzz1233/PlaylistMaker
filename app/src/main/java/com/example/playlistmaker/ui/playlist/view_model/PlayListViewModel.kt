package com.example.playlistmaker.ui.playlist.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayListViewModel(
    private val playListInteractor: PlayListInteractor
) : ViewModel() {
    val updatedPlaylist: MutableLiveData<Playlist> = MutableLiveData()
    fun getUpdatePlayListById(searchId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getUpdatePlayListById(searchId).collect {
                updatedPlaylist.postValue(it)
            }
        }
    }

    fun deleteTrackIfIsNotInPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getPlayList()
                .collect {
                    val id = track.trackId.toLong()
                    val isIdInAnyPlaylist = it.any { playlist ->
                        playlist.trackArray?.contains(id) == true
                    }
                    if (!isIdInAnyPlaylist) {
                        playListInteractor.deleteIfIsNotInPlaylist(id)
                    }
                }
        }
    }

    val trackList: MutableLiveData<List<Track>> = MutableLiveData(emptyList())
    fun getTrackList(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.getTrackList(playlist).collect { track ->
                trackList.postValue(track)
            }
        }
    }

    fun deletePlayList(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.deletePlaylist(playlist)
        }
    }

    private val _deletedTrack = MutableLiveData<Boolean>()
    val deletedTrack: LiveData<Boolean> = _deletedTrack
    fun deleteTrack(track: Track, playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            val originalTrackArraySize = playlist.trackArray.size
            playlist.trackArray = playlist.trackArray.filter { it?.toInt() != track.trackId }
            playlist.arrayNumber = playlist.arrayNumber?.minus(1)
            playListInteractor.update(track, playlist)
            if (originalTrackArraySize != playlist.trackArray.size) {
                playListInteractor.update(track, playlist)
                _deletedTrack.postValue(true)
            }
            deleteTrackIfIsNotInPlaylist(playlist,track)
        }

    }

    val playlistTime: MutableLiveData<String> = MutableLiveData("")
    fun getPlaylistTime(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playListInteractor.durationCounting(playlist).collect { readyTime ->
                playlistTime.postValue(readyTime)
            }
        }
    }


}

