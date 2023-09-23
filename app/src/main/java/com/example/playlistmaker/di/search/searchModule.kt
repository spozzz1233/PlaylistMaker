package com.example.playlistmaker.di.search


import com.example.playlistmaker.data.search.Impl.SearchRepositoryImpl
import com.example.playlistmaker.data.search.SearchRepository
import com.example.playlistmaker.domain.search.SearchInteractor
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.ui.search.view_model.SearchFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module





val searchModule = module {
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