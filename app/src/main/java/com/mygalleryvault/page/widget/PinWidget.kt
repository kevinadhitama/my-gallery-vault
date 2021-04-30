package com.mygalleryvault.page.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.mygalleryvault.databinding.LayoutPinWidgetBinding

/**
 * Create by kevin.adhitama pm 4/30/2021.
 */
class PinWidget(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: LayoutPinWidgetBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LayoutPinWidgetBinding.inflate(inflater, this, true)
        binding.pinView.addTextChangedListener {
            //todo real validation
            if (it?.length == 4) {
                deactivatePinWidget()
                val keyboard: InputMethodManager? =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                keyboard?.hideSoftInputFromWindow(windowToken, 0)
            }
        }
    }

    fun activatePinWidget(): Boolean {
        this.visibility = View.VISIBLE
        return true
    }

    private fun deactivatePinWidget(): Boolean {
        this.visibility = View.GONE
        binding.pinView.text?.clear()
        return false
    }
}