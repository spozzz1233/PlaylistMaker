package com.example.playlistmaker.data.playlist


import com.example.playlistmaker.data.converters.PlayListConverter
import com.example.playlistmaker.data.converters.TrackConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.playList.PlayListRepository
import com.example.playlistmaker.domain.playList.model.Playlist
import com.example.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlayListConverter,

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



    override fun update(track: Track, playlist: Playlist) {
        appDatabase.PlayListDao().updatePlaylist(converter.mapplaylistClassToEntity(playlist))
        appDatabase.trackListingDao().insertTrack(track)

    }
    override fun deleteIfIsNotInPlaylist(searchId :Long){
        appDatabase.trackListingDao().deleteIfisNotInPlaylist(searchId)
    }
    override fun getUpdatePlayListById(id:Int): Flow<Playlist> = flow{
        emit(
            converter.mapplaylistEntityToClass(appDatabase.PlayListDao().getUpdatePlayList(id))
        )
        return@flow
    }
    override fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        var trackList: List<Track> = emptyList()
        playlist.trackArray.map { id ->
            val entity = id?.let { appDatabase.trackListingDao().queryTrackId(searchId = it) } ?: return@map
            trackList = trackList + (TrackConverter().mapTrackEntityToTrack(entity))
        }
        emit(trackList)
    }
    override fun deletePlaylist(playlist: Playlist) {
        converter.mapplaylistClassToEntity(playlist)
            ?.let { appDatabase.PlayListDao().deletePlaylist(it) }
    }
    override fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        val newPlaylist = Playlist(
            playlist.playlistId,
            playlistName,
            description,
            uri,
            playlist.trackArray,
            playlist.arrayNumber
        )
        appDatabase.PlayListDao().updatePlaylist(
            converter.mapplaylistClassToEntity(newPlaylist)
        )
    }
    override fun durationCounting(playlist: Playlist): Flow<String> = flow {
        var generalTime = 0

        playlist.trackArray.forEach {
            val entity = it?.let { it1 -> appDatabase.trackListingDao().queryTrackId(searchId = it1) }
            val track = (entity?.let { it1 -> TrackConverter().mapTrackEntityToTrack(it1) })
            val time = track?.trackTimeMillis
            if (time != null) {
                generalTime += time / 1000
            }
        }
        val hours = generalTime / (60 * 60)
        val minutes = generalTime / 60
        val seconds = generalTime % 60
        val readyTime = if (hours == 0){
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        emit(readyTime)
    }

}