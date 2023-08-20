package com.example.playlistmaker.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.domain.search.model.HistoryTrack
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search.model.historyTracks
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class MusicHistory(private val context: Context) {
    private val TAG = "MusicHistory"
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun clearSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("SaveHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("history")
        editor.apply()
        historyTracks.clear()
        Log.d(TAG, "History cleared")
    }
    fun saveHistoryTrack(track: Track) {
        sharedPreferences = context.getSharedPreferences("SaveHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val existingTrackIndex = historyTracks.indexOfFirst { it.trackName == track.trackName && it.artistName == track.artistName}
        if (existingTrackIndex != -1) {

            val existingTrack = historyTracks.removeAt(existingTrackIndex)
            historyTracks.add(0, existingTrack)
        } else {
            val historyTrack = HistoryTrack(
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl
            )
            historyTracks.add(0, historyTrack)
        }

        if (historyTracks.size >= 10) {
            historyTracks.removeAt(0)
        }

        val updatedJsonHistory = Gson().toJson(historyTracks)
        editor.putString("history", updatedJsonHistory)
        editor.apply()

    }
    fun getHistory() {
        sharedPreferences = context.getSharedPreferences("SaveHistory", Context.MODE_PRIVATE)
        val jsonHistory = sharedPreferences.getString("history", null)
        if (jsonHistory != null) {
            val type = object : TypeToken<ArrayList<HistoryTrack>>() {}.type
            val loadedTracks: ArrayList<HistoryTrack> = gson.fromJson(jsonHistory, type)
            historyTracks.clear()
            historyTracks.addAll(loadedTracks)
        }
    }
    fun curentPosition(track:HistoryTrack){
        val existingTrackIndex = historyTracks.indexOfFirst { it.trackName == track.trackName && it.artistName == track.artistName}

        val existingTrack = historyTracks.removeAt(existingTrackIndex)

//        val historyTrack = HistoryTrack(
//            track.trackName,
//            track.artistName,
//            track.trackTimeMillis,
//            track.artworkUrl100,
//            track.collectionName,
//            track.releaseDate,
//            track.primaryGenreName,
//            track.country,
//            track.previewUrl
//        )
        historyTracks.add(0, existingTrack)
    }

}
