package com.example.playlistmaker.data.playlist

import com.example.playlistmaker.data.converters.PlayListConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.playList.PlayListRepository
import com.example.playlistmaker.domain.playList.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlayListConverter
): PlayListRepository {

    override fun createPlayList(playlistName: String, description: String?, uri: String) {
        val playlist = Playlist(
            null,
            playlistName,
            description,
            uri,
            emptyList(),
            0
        )
        appDatabase.PlayListDao().insertPlayList(converter.mapplaylistClassToEntity(playlist))
    }

    override fun getPlayList(): Flow<List<Playlist>> = flow {
        val playlistConverted = withContext(Dispatchers.IO) {
            appDatabase.PlayListDao().getPlayList()
                .map { converter.mapplaylistEntityToClass(it) }
        }
        emit(playlistConverted)
    }
}