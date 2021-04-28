package com.mygalleryvault.page.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.mygalleryvault.databinding.FragmentAlbumsBinding
import com.mygalleryvault.page.ui.albums.adapter.AlbumsListAdapter
import com.mygalleryvault.utils.view.DefaultGridItemDecorator

class AlbumsFragment : Fragment() {
    private val albumSpan = 2
    private val albumsViewModel: AlbumsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAlbumsBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = GridLayoutManager(context, albumSpan)
        binding.recyclerView.adapter = AlbumsListAdapter(albumsViewModel.albums)
        binding.recyclerView.addItemDecoration(DefaultGridItemDecorator(albumSpan))
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        return binding.root
    }
}
