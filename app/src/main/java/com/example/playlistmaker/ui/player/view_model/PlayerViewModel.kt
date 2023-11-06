package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.search.model.Track

class PlayerViewModel(
    private val mediaInteractor: MediaInteractor,
    private val favoriteInteractor: FavoriteInteractor
): ViewModel(){


    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying
    private val _addTrack = MutableLiveData<Boolean>()
    val addTrack: LiveData<Boolean> = _addTrack

    private val _deleteTrack = MutableLiveData<Boolean>()
    val deleteTrack: LiveData<Boolean> = _deleteTrack


    fun FavoriteTrack(track: Track) {
        if (track.isFavorite) {
            favoriteInteractor.deleteTrack(track)
        } else{
            favoriteInteractor.addTrack(track)
        }
    }

    fun isPlaying(): Boolean{
        return mediaInteractor.isPlaying()
    }
    fun getCurrentPosition(): Int{
        return mediaInteractor.getCurrentPosition()
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

}
