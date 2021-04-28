package com.mygalleryvault.datamodel

/**
 * Create by kevin.adhitama pm 4/26/2021.
 */
data class Album(
    val id: String,
    val name: String,
    val tsCreated: String,
    val listImage: MutableList<Picture>
)
