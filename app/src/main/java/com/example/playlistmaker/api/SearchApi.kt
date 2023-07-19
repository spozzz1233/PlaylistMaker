package com.example.playlistmaker.api

import android.view.View
import com.example.playlistmaker.R
import com.example.playlistmaker.model.tracks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<tracksResponse>


}


