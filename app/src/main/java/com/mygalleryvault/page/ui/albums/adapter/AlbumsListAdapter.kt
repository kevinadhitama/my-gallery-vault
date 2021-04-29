package com.mygalleryvault.page.ui.albums.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

    fun updateItems(albumsList: MutableList<Album>) {
        this.albumsList.clear()
        this.albumsList.addAll(albumsList)
        notifyDataSetChanged()
    }

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

        //todo replace with real implementation
        Glide.with(holder.itemView.context)
            //.load("https://lh3.googleusercontent.com/proxy/qL9T3Z1GWABr0kqsLyv_LnldBcn-10KuC8gKFuS4zlmJn_-EdP2LQEpjxpSl3MN213bRlm8QTBskDqAmuuo1o0FSgDtn2eNY8PqmJpf9wWaKu7LSkFI")
            .load("/storage/emulated/0/Android/data/com.mygalleryvault/files/770b4260-faaa-4086-85a9-73c749303af85904535728797528553")
            .centerCrop()
            .dontAnimate()
            .into(holder.binding.albumImageView)
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