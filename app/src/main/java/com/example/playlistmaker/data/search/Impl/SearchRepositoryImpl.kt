package com.example.playlistmaker.data.search.Impl

import android.content.Context
import android.net.ConnectivityManager
import com.example.playlistmaker.data.api.SearchApi
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.model.tracks
import com.example.playlistmaker.data.search.SearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepositoryImpl(private val context: Context): SearchRepository {
    override fun searchTrack(query: String, callback: (success: Boolean) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val searchApi = retrofit.create(SearchApi::class.java)

        if (query.isNotEmpty()) {
            searchApi.search(query).enqueue(object : Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true){
                            callback(true)
                            tracks.addAll(response.body()?.results!!)
                        }
                    }
                    if(tracks.isEmpty()){
                        callback(true)
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    if(!checkInternetConnection()){
                        callback(false)
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


//override fun onResponse(call: Call<TracksResponse>, response: Response<TracksResponse>) {
//    if (response.code() == 200) {
//        tracks.clear()
//        if (response.body()?.results?.isNotEmpty() == true){
//            callback(true)
//        }
//    }
//    if (tracks.isEmpty()){
//        callback(false)
//    }
//
//
//}


//class SearchRepositoryImpl(private val context: Context,val callback: SearchCallback): SearchRepository{
//    override fun searchTrack(query: String) {
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://itunes.apple.com")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val searchApi = retrofit.create(SearchApi::class.java)
//
//        val searchAdapter = searchAdapter(context)
//        if (query.isNotEmpty()) {
//            callback.progressBarVisible()
//            callback.recyclerViewSearchGone()
//            searchApi.search(query).enqueue(object :
//                Callback<TracksResponse> {
//                override fun onResponse(
//                    call: Call<TracksResponse>,
//                    response: Response<TracksResponse>
//                ) {
//                    callback.progressBarGone()
//                    if (response.code() == 200) {
//                        tracks.clear()
//                        if (response.body()?.results?.isNotEmpty() == true) {
//                            callback.recyclerViewSearchVisible()
//
//                            searchAdapter.notifyDataSetChanged()
//                        }
//                        if (tracks.isEmpty()) {
//                            callback.noResultVisible()
//                            callback.historyGone()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
//                    if (!checkInternetConnection()) {
//                        callback.noInternetVisible()
//                        callback.historyGone()
//                        callback.progressBarGone()
//                    }
//                }
//            })
//        }
//    }
//    override fun checkInternetConnection(): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }
//    override fun getHistoryTracks() {
//        TODO("Not yet implemented")
//    }
//}
