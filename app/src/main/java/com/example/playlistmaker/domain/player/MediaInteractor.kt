package com.example.playlistmaker.domain.player

interface MediaInteractor {
    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit)
    fun startPlayer(onPlaybackStarted: () -> Unit)
    fun pausePlayer(onPlaybackPaused: () -> Unit)
    fun stopPlayer()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun getPlayerState(): Int
}