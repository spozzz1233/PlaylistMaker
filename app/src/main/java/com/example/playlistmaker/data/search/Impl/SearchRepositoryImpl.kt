package com.example.playlistmaker.data.search.Impl

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import com.example.playlistmaker.data.api.SearchApi
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.callback.SearchCallback
import com.example.playlistmaker.domain.model.tracks
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.presentation.adapter.searchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepositoryImpl(private val context: Context,val searchCallback: SearchCallback): SearchRepository{
    override fun searchTrack(query: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val searchApi = retrofit.create(SearchApi::class.java)

        val searchAdapter = searchAdapter(context)
        Log.d("QueryValue", "Значение query: $query")
        if (query.isNotEmpty()) {
            searchCallback.progressBarVisible()
            searchCallback.recyclerViewSearchGone()
            Log.d("CallbackTrigger", "Колбек сработал")
            searchApi.search(query).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    searchCallback.progressBarGone()
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            searchCallback.recyclerViewSearchVisible()
                            tracks.addAll(response.body()?.results!!)
                            searchAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            searchCallback.noResultVisible()
                            searchCallback.historyGone()
                        }
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    if (!checkInternetConnection()) {
                        searchCallback.noInternetVisible()
                        searchCallback.historyGone()
                        searchCallback.progressBarGone()
                    }
                }
            })
        }
    }
    override fun checkInternetConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    override fun getHistoryTracks() {
        TODO("Not yet implemented")
    }
}
