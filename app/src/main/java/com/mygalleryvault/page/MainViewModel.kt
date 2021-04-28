package com.mygalleryvault.page

import androidx.lifecycle.ViewModel
import com.mygalleryvault.datamodel.Album

class MainViewModel : ViewModel() {

    val albums = mutableListOf<Album>().apply {
        //todo remove dummy data
        this.add(Album("Lover Album", "2 Days ago", mutableListOf()))
        this.add(Album("Ex Album", "Weeks ago", mutableListOf()))
        this.add(Album("Wife Album", "A year ago", mutableListOf()))
    }
}
