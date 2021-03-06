package com.mygalleryvault.page.ui.albumdetail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mygalleryvault.R
import com.mygalleryvault.const.PublicConstants
import com.mygalleryvault.databinding.FragmentImagesThumbnailsListBinding
import com.mygalleryvault.datamodel.Picture
import com.mygalleryvault.page.MainViewModel
import com.mygalleryvault.page.dialogs.FullScreenImageDialog
import com.mygalleryvault.page.ui.albumdetail.adapter.ImagesThumbnailsListAdapter
import com.mygalleryvault.utils.AlbumSharedPreferences
import com.mygalleryvault.utils.view.DefaultGridItemDecorator
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImagesThumbnailsListFragment : Fragment() {

    private lateinit var binding: FragmentImagesThumbnailsListBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val args: ImagesThumbnailsListFragmentArgs by navArgs()
    private val albumSpan = 3
    private var tempSavedPicture: Picture? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagesThumbnailsListBinding.inflate(inflater, container, false)
        mainViewModel.toolbarTitle.value = args.album.name

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
        binding.recyclerView.adapter = ImagesThumbnailsListAdapter(args.album.listImage,
            object : ImagesThumbnailsListAdapter.Listener {
                override fun onItemClickListener(item: Picture) {
                    FullScreenImageDialog(item).show(parentFragmentManager, "imageDialog")
                }

                override fun onItemLongClickListener(item: Picture) {
                    //todo remove item listener
                }
            })
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.addItemDecoration(DefaultGridItemDecorator(albumSpan, 2f))
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    //todo: show error message
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        PublicConstants.FILE_PROVIDER_PATH,
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        PublicConstants.TAKE_PICTURE_REQUEST_CODE
                    )
                }
            }
        }
    }

    private fun dispatchChooseGalleryIntent() {
        Intent(Intent.ACTION_GET_CONTENT).also { pickPhoto ->
            pickPhoto.type = "image/*"
            pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(
                pickPhoto,
                PublicConstants.TAKE_PICTURE_GALLERY_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PublicConstants.TAKE_PICTURE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    tempSavedPicture?.let {
                        AlbumSharedPreferences.addImage(requireContext(), args.album, it)
                        binding.recyclerView.adapter!!.notifyDataSetChanged()
                    }
                }
                tempSavedPicture = null
            }
            PublicConstants.TAKE_PICTURE_GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    data?.data?.let {
                        createImagesFile(mutableListOf(it))
                    }
                    data?.clipData?.let {
                        val listUri = mutableListOf<Uri>()
                        for (i in 0 until it.itemCount) {
                            listUri.add(it.getItemAt(i).uri)
                        }
                        createImagesFile(listUri)
                    }
                }
            }
        }
    }

    private fun createImageFile(): File {
        val fileName: String = UUID.randomUUID().toString()
        val storageDir: File? = activity?.getExternalFilesDir("")
        return File.createTempFile(
            fileName,
            "",
            storageDir
        ).apply {
            tempSavedPicture =
                Picture(absolutePath, fileName, Calendar.getInstance().timeInMillis.toString())
        }
    }

    private fun createImagesFile(listUri: MutableList<Uri>) {
        listUri.forEach {
            val fileName: String = UUID.randomUUID().toString()
            val storageDir: File? = activity?.getExternalFilesDir("")
            File.createTempFile(
                fileName,
                "",
                storageDir
            ).apply {
                requireContext().contentResolver.openInputStream(it)?.let { input ->
                    val out = FileOutputStream(this)
                    val buf = ByteArray(1024)
                    var len: Int
                    while (input.read(buf).also { len = it } > 0) {
                        out.write(buf, 0, len)
                    }
                    out.close()
                    input.close()
                    val picture =
                        Picture(
                            this.absolutePath,
                            fileName,
                            Calendar.getInstance().timeInMillis.toString()
                        )

                    AlbumSharedPreferences.addImage(requireContext(), args.album, picture)
                }
            }
        }
        binding.recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun showAlbumDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.hint_add_photos_to_album, args.album.name))
            .setItems(R.array.dialog_add_photo_options) { _, which ->
                when (which) {
                    0 -> {
                        dispatchTakePictureIntent()
                    }
                    1 -> {
                        dispatchChooseGalleryIntent()
                    }
                }
            }
            .setCancelable(false)
            .show()
    }
}
