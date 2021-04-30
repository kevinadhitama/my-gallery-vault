package com.mygalleryvault.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mygalleryvault.datamodel.Album

class MainViewModel(listAlbums: MutableList<Album>) : ViewModel() {

    private val _albums = MutableLiveData<MutableList<Album>>().apply {
        value = listAlbums
    }

    private fun notifyAlbums() {
        this._albums.value = this._albums.value
    }

    fun addAlbum(album: Album) {
        _albums.value?.add(0, album)
        notifyAlbums()
    }

    val album : LiveData<MutableList<Album>> = _albums

    val toolbarTitle = MutableLiveData<String>()
}
