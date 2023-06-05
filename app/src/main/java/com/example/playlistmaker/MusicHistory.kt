package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class MusicHistory(private val context: Context) {
    private val TAG = "MusicHistory"
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun getHistoryTracks(context: Context): List<HistoryTrack> {
        sharedPreferences = context.getSharedPreferences("SaveHistory", Context.MODE_PRIVATE)
        val jsonHistory = sharedPreferences.getString("history", null)
        val type = object : TypeToken<List<HistoryTrack>>() {}.type
        val historyTracks = gson.fromJson<List<HistoryTrack>>(jsonHistory, type) ?: emptyList()
        Log.d(TAG, "List size: ${historyTracks.size}")
        return historyTracks
    }

    fun clearSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("SaveHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("history")
        editor.apply()
        Log.d(TAG, "History cleared")
    }
    fun saveHistoryTrack(track: Track) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("SaveHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val jsonHistory = sharedPreferences.getString("history", null)
        val historyList = if (jsonHistory != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            Gson().fromJson<ArrayList<Track>>(jsonHistory, type)
        } else {
            ArrayList()
        }

        historyList.add(track)


        if (historyList.size > 10) {
            historyList.removeAt(0)
        }

        val updatedJsonHistory = Gson().toJson(historyList)
        editor.putString("history", updatedJsonHistory)
        editor.apply()


        val historyTrack = HistoryTrack(
            track.trackName,
            track.artistName,
            track.trackTimeMillis.toInt(),
            track.artworkUrl100
        )
        historyTracks.add(0, historyTrack)
    }

}
