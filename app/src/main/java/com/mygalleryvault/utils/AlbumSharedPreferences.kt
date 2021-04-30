package com.mygalleryvault.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mygalleryvault.datamodel.Album
import com.mygalleryvault.datamodel.Picture
import java.util.*

/**
 * Create by kevin.adhitama pm 4/27/2021.
 */
object AlbumSharedPreferences {
    private const val albumSPName = "Albums"

    private fun getSP(context: Context): SharedPreferences {
        return context.getSharedPreferences(albumSPName, Context.MODE_PRIVATE)
    }

    fun addImage(context: Context, album: Album, picture: Picture) {
        val sp = getSP(context)
        album.listImage.add(0, picture)
        with(sp.edit()) {
            putString(album.id, Gson().toJson(album))
            apply()
        }
    }

    fun addAlbum(context: Context, albumName: String): Album? {
        if (albumName.isEmpty()) return null

        val album = Album(
            UUID.randomUUID().toString(),
            albumName,
            Calendar.getInstance().timeInMillis.toString(),
            mutableListOf()
        )

        val sp = getSP(context)
        with(sp.edit()) {
            putString(album.id, Gson().toJson(album))
            apply()
        }
        return album
    }

    fun deleteAlbum(context: Context, album: Album) {
        val sp = getSP(context)
        with(sp.edit()) {
            remove(album.id)
            apply()
        }
    }

    fun getListAlbum(context: Context): MutableList<Album> {
        val sp = getSP(context)
        val list = sp.all.map {
            Gson().fromJson(it.value.toString(), object : TypeToken<Album>() {}.type) as Album
        }.toMutableList()
        list.sortByDescending { it.tsCreated }
        return list
    }
}