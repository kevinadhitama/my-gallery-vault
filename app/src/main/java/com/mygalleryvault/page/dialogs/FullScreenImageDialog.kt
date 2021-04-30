package com.mygalleryvault.page.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mygalleryvault.R
import com.mygalleryvault.databinding.DialogFullScreenImageViewerBinding
import com.mygalleryvault.datamodel.Picture
import com.mygalleryvault.utils.StringUtil

/**
 * Create by kevin.adhitama pm 4/30/2021.
 */
class FullScreenImageDialog(private val picture: Picture) : DialogFragment() {

    lateinit var binding: DialogFullScreenImageViewerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFullScreenImageViewerBinding.inflate(inflater, container, false)
        Glide.with(this)
            .load(picture.filePath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageView)
        binding.textViewDescription.text = resources.getString(R.string.common_added_relative_time, StringUtil.getRelativeTime(picture.tsCreated.toLong()))
        binding.imageView.setOnClickListener {

        }
        binding.root.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.attributes = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.MATCH_PARENT
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS)
        return dialog
    }
}