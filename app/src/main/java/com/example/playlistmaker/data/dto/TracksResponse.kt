package com.example.playlistmaker.data.dto
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.domain.search.model.Track

class TracksResponse(val results: ArrayList<Track>): Response()
