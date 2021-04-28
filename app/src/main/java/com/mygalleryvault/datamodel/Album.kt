package com.mygalleryvault.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Create by kevin.adhitama pm 4/26/2021.
 */
@Parcelize
data class Album(
    val id: String,
    val name: String,
    val tsCreated: String,
    val listImage: MutableList<Picture>
) : Parcelable
