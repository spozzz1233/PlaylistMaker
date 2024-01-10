package com.example.playlistmaker.data.player


interface MediaRepository {
    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit)
    fun startPlayer(onPlaybackStarted: () -> Unit)
    fun pausePlayer(onPlaybackPaused: () -> Unit)
    fun stopPlayer()
    fun isPlaying(): Boolean
    fun getPlayerState(): Int
    fun getCurrentPosition(): Int
}
