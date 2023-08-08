package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.MediaInteractor

class PlayerViewModel(
    private val mediaInteractor: MediaInteractor
): ViewModel(){


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
