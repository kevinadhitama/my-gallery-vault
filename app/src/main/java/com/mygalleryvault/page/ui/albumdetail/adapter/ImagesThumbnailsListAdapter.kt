package com.mygalleryvault.page.ui.albumdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mygalleryvault.databinding.ItemImageThumbnailBinding
import com.mygalleryvault.datamodel.Picture

/**
 * Create by kevin.adhitama pm 4/28/2021.
 */
class ImagesThumbnailsListAdapter(
    private val albumsList: List<Picture>,
    private val listener: Listener
) :
    RecyclerView.Adapter<ImagesThumbnailsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageThumbnailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = albumsList[position]
        Glide.with(holder.binding.root.context)
            .load(item.filePath)
            .centerCrop()
            .thumbnail()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.thumbnailImageView)

        holder.binding.root.setOnClickListener {
            listener.onItemClickListener(item)
        }
        holder.binding.root.setOnLongClickListener {
            listener.onItemLongClickListener(item)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    interface Listener {
        fun onItemClickListener(item: Picture)

        fun onItemLongClickListener(item: Picture)
    }

    inner class ViewHolder(val binding: ItemImageThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root)
}