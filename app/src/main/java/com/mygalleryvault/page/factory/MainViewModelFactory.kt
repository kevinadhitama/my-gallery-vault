package com.mygalleryvault.page.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.mygalleryvault.datamodel.Album
import com.mygalleryvault.page.MainViewModel
import com.mygalleryvault.utils.AlbumSharedPreferences

/**
 * Create by kevin.adhitama pm 4/28/2021.
 */
class MainViewModelFactory(context: Context) : NewInstanceFactory() {
    private var listAlbums: MutableList<Album> = AlbumSharedPreferences.getListAlbum(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (MainViewModel::class.java.isAssignableFrom(modelClass)) {
            return MainViewModel(listAlbums) as T
        }

        return super.create(modelClass)
    }
}