package com.example.playlistmaker.domain

import com.example.playlistmaker.data.MediaRepository

class MediaUseCase(private val mediaRepository: MediaRepository) {
    fun preparePlayer(trackUrl: String, onPrepared: () -> Unit) {
        mediaRepository.preparePlayer(trackUrl, onPrepared)
    }

    fun startPlayer(onPlaybackStarted: () -> Unit) {
        mediaRepository.startPlayer(onPlaybackStarted)
    }

    fun pausePlayer(onPlaybackPaused: () -> Unit) {
        mediaRepository.pausePlayer(onPlaybackPaused)
    }

    fun stopPlayer() {
        mediaRepository.stopPlayer()
    }

    fun isPlaying(): Boolean {
        return mediaRepository.isPlaying()
    }

    fun getCurrentPosition(): Int {
        return mediaRepository.getCurrentPosition()
    }
}

