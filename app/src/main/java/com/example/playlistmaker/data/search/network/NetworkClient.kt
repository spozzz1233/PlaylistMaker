package com.example.playlistmaker.data.search.network

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}