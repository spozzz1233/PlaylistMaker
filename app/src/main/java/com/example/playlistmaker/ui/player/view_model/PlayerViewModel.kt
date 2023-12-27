package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favorite.FavoriteInteractor
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaInteractor: MediaInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playListInteractor: PlayListInteractor
): ViewModel(){
    val favouritLiveData = MutableLiveData<Boolean>()
    private val _isPlaying = MutableLiveData<Boolean>()
    val playListList: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>(emptyList())
    val playListAdding =MutableLiveData(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying
    fun addTrack(track: Track, playlist: Playlist) {
        if (playlist.trackArray.contains(track.trackId.toLong())) {
            playListAdding.postValue(true)
        } else {
            playListAdding.postValue(false)
            playlist.trackArray = (playlist.trackArray + track.trackId.toLong())!!
            playlist.arrayNumber = (playlist.arrayNumber?.plus(1))!!
            viewModelScope.launch(Dispatchers.IO){
                playListInteractor.update(track, playlist)
            }

        }
    }

    fun FavoriteTrack(track: Track) {
        if (track.isFavorite) {
            viewModelScope.launch(Dispatchers.IO) {
                track.trackId?.let{favoriteInteractor.deleteTrack(track)}
            }
        } else{
            viewModelScope.launch(Dispatchers.IO) {
                track.trackId?.let{favoriteInteractor.addTrack(track)}
            }
        }
    }

    fun isPlaying(): Boolean{
        return mediaInteractor.isPlaying()
    }
    fun getCurrentPosition(): Int{
        return mediaInteractor.getCurrentPosition()
    }
    fun getPlayerState() : Int{
        return mediaInteractor.getPlayerState()
    }
    fun formatTime(milliseconds: Int): String {
        val totalSeconds = (milliseconds + 999) / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    fun startPlayer(onStarted: () -> Unit){
        mediaInteractor.startPlayer{
            _isPlaying.postValue(true)
            onStarted.invoke()
        }
    }
    fun pausePlayer(onPlaybackPaused: () -> Unit ){
        return mediaInteractor.pausePlayer{
            _isPlaying.postValue(false)
            onPlaybackPaused.invoke()
        }
    }
    fun stopPlayer(){
        return mediaInteractor.stopPlayer()
    }
    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit) {
        mediaInteractor.preparePlayer(trackUrl){
            _isPlaying.postValue(false)
            onPrepared.invoke()
        }
    }
    fun checkTrackInFavorite(track: Track): LiveData<Boolean> {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(300)
                track.trackId?.let { id ->
                    favoriteInteractor.checkTreckInFavorite(id)
                        .collect { value ->
                            favouritLiveData.postValue(value)
                        }
                }
            }
        }
        return favouritLiveData
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playListInteractor.getPlayList()
                .collect {
                    if (it.isNotEmpty()) {
                        playListList.postValue(it)
                    } else {
                        playListList.postValue(emptyList())
                    }
                }
        }
    }


}
