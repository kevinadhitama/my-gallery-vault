package com.mygalleryvault.page.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mygalleryvault.R
import com.mygalleryvault.databinding.DialogCreateAlbumBinding
import com.mygalleryvault.databinding.FragmentAlbumsBinding
import com.mygalleryvault.datamodel.Album
import com.mygalleryvault.page.MainViewModel
import com.mygalleryvault.page.ui.albums.adapter.AlbumsListAdapter
import com.mygalleryvault.utils.AlbumSharedPreferences
import com.mygalleryvault.utils.view.DefaultGridItemDecorator

class AlbumsFragment : Fragment() {
    private lateinit var albumListAdapter: AlbumsListAdapter
    private lateinit var binding: FragmentAlbumsBinding

    private val albumSpan = 2
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumsBinding.inflate(inflater, container, false)

        initFAB()
        initRecyclerView()
        return binding.root
    }

    private fun initFAB() {
        val fab: FloatingActionButton = binding.fab
        fab.setOnClickListener {
            showAlbumDialog()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, albumSpan)
        binding.recyclerView.addItemDecoration(DefaultGridItemDecorator(albumSpan))
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.setHasFixedSize(true)

        mainViewModel.album.value?.let {
            albumListAdapter = AlbumsListAdapter(it, getRecyclerViewListener())
        }
        binding.recyclerView.adapter = albumListAdapter
    }

    private fun getRecyclerViewListener(): AlbumsListAdapter.Listener {
        return object : AlbumsListAdapter.Listener {
            override fun onItemClickListener(item: Album) {
                binding.root.findNavController().navigate(
                    AlbumsFragmentDirections.actionNavAlbumsToImagesThumbnailsList(item)
                )
            }

            override fun onItemLongClickListener(item: Album) {

            }
        }
    }

    private fun showAlbumDialog() {
        val dialogLayout = DialogCreateAlbumBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogLayout.root)
            .setPositiveButton(R.string.common_text_create) { _, _ ->
                AlbumSharedPreferences.addAlbum(
                    requireContext(),
                    dialogLayout.textField.text.toString()
                )?.let {
                    mainViewModel.addAlbum(it)
                    mainViewModel.album.value?.let { albums ->
                        albumListAdapter.updateItems(albums)
                        binding.recyclerView.postDelayed({
                            binding.recyclerView.smoothScrollToPosition(0)
                        }, 500)
                    }
                }
            }
            .setNeutralButton(R.string.common_text_cancel) { _, _ ->
                // Respond to negative button press
            }
            .setCancelable(false)
            .show()
        dialogLayout.textField.requestFocus()
    }
}
