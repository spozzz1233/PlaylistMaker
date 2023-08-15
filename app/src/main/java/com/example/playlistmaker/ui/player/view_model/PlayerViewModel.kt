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

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int>
        get() = _currentPosition

    init {
        _isPlaying.value = false
        _currentPosition.value = 0
    }

//    fun updateIsPlaying(isPlaying: Boolean) {
//        _isPlaying.value = isPlaying
//    }
//
//    fun updateCurrentPosition(position: Int) {
//        _currentPosition.value = position
//    }


    fun isPlaying(): Boolean{
        return mediaInteractor.isPlaying()
    }
    fun getCurrentPosition(): Int{
        return mediaInteractor.getCurrentPosition()
    }
    fun startPlayer(onStarted: () -> Unit){
        return mediaInteractor.startPlayer(onStarted)
    }
    fun pausePlayer(onPlaybackPaused: () -> Unit ){
        return mediaInteractor.pausePlayer(onPlaybackPaused)
    }
    fun stopPlayer(){
        return mediaInteractor.stopPlayer()
    }
    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit) {
        mediaInteractor.preparePlayer(trackUrl, onPrepared)
    }
}
