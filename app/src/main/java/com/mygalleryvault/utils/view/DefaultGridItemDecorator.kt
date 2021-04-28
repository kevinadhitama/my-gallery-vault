package com.mygalleryvault.utils.view

import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * Create by kevin.adhitama pm 4/28/2021.
 */
class DefaultGridItemDecorator(private val spanCount: Int, private val dpSpacing: Float = 8f) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        val column: Int = position % spanCount

        val spacingInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpSpacing,
            parent.context.resources.displayMetrics
        ).toInt()

        outRect.left = spacingInPx - column * spacingInPx / spanCount
        outRect.right = (column + 1) * spacingInPx / spanCount

        if (position < spanCount) {
            outRect.top = spacingInPx
        }
        outRect.bottom = spacingInPx
    }
}