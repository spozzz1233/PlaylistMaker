package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.data.player.MediaRepository

class MediaInteractorImpl(private val mediaRepository: MediaRepository) : MediaInteractor {
    override fun preparePlayer(trackUrl: String, onPrepared: () -> Unit) {
        mediaRepository.preparePlayer(trackUrl, onPrepared)
    }

    override fun startPlayer(onPlaybackStarted: () -> Unit) {
        mediaRepository.startPlayer(onPlaybackStarted)
    }

    override fun pausePlayer(onPlaybackPaused: () -> Unit) {
        mediaRepository.pausePlayer(onPlaybackPaused)
    }

    override fun stopPlayer() {
        mediaRepository.stopPlayer()
    }

    override fun isPlaying(): Boolean {
        return mediaRepository.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return mediaRepository.getCurrentPosition()
    }

    override fun getPlayerState(): Int {
        return mediaRepository.getPlayerState()
    }
}

