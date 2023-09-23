package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.MediaInteractor

class PlayerViewModel(
    private val mediaInteractor: MediaInteractor
): ViewModel(){


    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

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
