package com.mygalleryvault.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Create by kevin.adhitama pm 4/26/2021.
 */
@Parcelize
data class Picture(
    val filePath: String,
    val fileName: String,
    val tsCreated: String
) : Parcelable
