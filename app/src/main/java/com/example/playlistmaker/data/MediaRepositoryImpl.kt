package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.MediaRepository

class MediaRepositoryImpl : MediaRepository {
    private var mediaPlayer: MediaPlayer? = null
    private var playerState = STATE_DEFAULT

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    override fun preparePlayer(trackUrl: String, onPrepared: () -> Unit) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.let {
            it.setDataSource(trackUrl)
            it.prepareAsync()
            it.setOnPreparedListener { _ ->
                playerState = STATE_PREPARED
                onPrepared.invoke()
            }
            it.setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        }
    }

    override fun startPlayer(onPlaybackStarted: () -> Unit) {
        mediaPlayer?.let {
            it.start()
            playerState = STATE_PLAYING
            onPlaybackStarted.invoke()
        }
    }

    override fun pausePlayer(onPlaybackPaused: () -> Unit) {
        mediaPlayer?.let {
            it.pause()
            playerState = STATE_PAUSED
            onPlaybackPaused.invoke()
        }
    }

    override fun stopPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
}
