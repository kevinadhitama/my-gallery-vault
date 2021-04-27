package com.mygalleryvault.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mygalleryvault.datamodel.Album

/**
 * Create by kevin.adhitama pm 4/27/2021.
 */
object AlbumSharedPreferences {
    private const val albumSPName = "Albums"
    private const val albumSPKey = "listAlbums"

    fun setListAlbum(context: Context, albums: List<Album>) {
        val sp = context.getSharedPreferences(albumSPName, Context.MODE_PRIVATE)
        with(sp.edit()) {
            putString(albumSPKey, Gson().toJson(albums))
            apply()
        }
    }

    fun getListAlbum(context: Context): List<Album> {
        val sp = context.getSharedPreferences(albumSPName, Context.MODE_PRIVATE)
        sp.getString(albumSPKey, null)?.let {
            return Gson().fromJson(it, object : TypeToken<List<Album>>() {}.type)
        } ?: run {
            return mutableListOf()
        }
    }
}