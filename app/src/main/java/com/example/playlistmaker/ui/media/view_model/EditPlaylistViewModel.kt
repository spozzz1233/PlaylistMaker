package com.example.playlistmaker.ui.media.view_model
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.playList.PlayListInteractor
import com.example.playlistmaker.domain.playList.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlayListInteractor
) : ViewModel() {

    fun savePlayList(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        viewModelScope.launch(Dispatchers.IO){
            playlistInteractor.savePlaylist(playlist, playlistName, description, uri)
        }
    }
    val updatedPlaylist : MutableLiveData<Playlist> = MutableLiveData()
    fun getUpdatePlayListById (searchId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getUpdatePlayListById(searchId).collect{
                updatedPlaylist.postValue(it)
            }
        }
    }
}