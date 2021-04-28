package com.mygalleryvault.page

import androidx.lifecycle.ViewModel
import com.mygalleryvault.datamodel.Album

class MainViewModel : ViewModel() {

    val albums = mutableListOf<Album>()
}
