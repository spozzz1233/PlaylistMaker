package com.example.playlistmaker.di.search


import com.example.playlistmaker.data.api.SearchApi
import com.example.playlistmaker.data.search.Impl.SearchRepositoryImpl
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.ui.search.view_model.SearchFragmentViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val searchModule = module {
    single<SearchApi> {

        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApi::class.java)

    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), get())
    }

    single<SearchRepository>{
        SearchRepositoryImpl(get())
    }

    single<SearchInteractor>{
        SearchInteractorImpl(get())
    }

    viewModel {
        SearchFragmentViewModel(get())
    }
}