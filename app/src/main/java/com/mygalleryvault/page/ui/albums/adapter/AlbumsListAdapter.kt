package com.mygalleryvault.page.ui.albums.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mygalleryvault.R
import com.mygalleryvault.databinding.ItemAlbumBinding
import com.mygalleryvault.datamodel.Album
import com.mygalleryvault.utils.StringUtil

/**
 * Create by kevin.adhitama pm 4/28/2021.
 */
class AlbumsListAdapter(
    private val albumsList: MutableList<Album>,
    private val listener: Listener
) : RecyclerView.Adapter<AlbumsListAdapter.ViewHolder>() {

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
        val item = albumsList[position]
        holder.binding.albumTitleTextView.text = item.name
        holder.binding.albumSubtitleTextView.text =
            StringUtil.getRelativeTime(item.tsCreated.toLong())
        holder.binding.root.setOnClickListener {
            listener.onItemClickListener(item)
        }
        holder.binding.root.setOnLongClickListener {
            listener.onItemLongClickListener(item)
            return@setOnLongClickListener true
        }

        if (item.listImage.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.listImage[0].filePath)
                .centerCrop()
                .thumbnail()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.albumImageView)
        } else {
            holder.binding.albumImageView.setImageResource(R.drawable.placeholder_no_image)
        }
    }

    override fun getItemCount(): Int {
        return albumsList.size
    }

    interface Listener {
        fun onItemClickListener(item: Album)

        fun onItemLongClickListener(item: Album)
    }

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)
}