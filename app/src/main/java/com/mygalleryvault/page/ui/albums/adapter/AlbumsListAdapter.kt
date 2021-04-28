package com.mygalleryvault.page.ui.albums.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.mygalleryvault.R
import com.mygalleryvault.databinding.ItemAlbumBinding
import com.mygalleryvault.datamodel.Album

/**
 * Create by kevin.adhitama pm 4/28/2021.
 */
class AlbumsListAdapter(private val albumsList: List<Album>) :
    RecyclerView.Adapter<AlbumsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.albumTitleTextView.text = albumsList[position].name
        holder.binding.albumSubtitleTextView.text = albumsList[position].tsCreated
        holder.binding.root.animation =
            AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.fade_in_animation)
    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)
}