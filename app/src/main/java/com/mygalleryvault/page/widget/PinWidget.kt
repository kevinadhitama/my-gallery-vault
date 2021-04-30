package com.mygalleryvault.page.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.mygalleryvault.R
import com.mygalleryvault.databinding.LayoutPinWidgetBinding
import com.mygalleryvault.utils.PassCodeSharedPreferences

/**
 * Create by kevin.adhitama pm 4/30/2021.
 */
class PinWidget(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var binding: LayoutPinWidgetBinding
    private var tempPassCode = ""
    private lateinit var firstTimeUserListener: TextWatcher
    private lateinit var normalUserListener: TextWatcher

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = LayoutPinWidgetBinding.inflate(inflater, this, true)

        if (PassCodeSharedPreferences.hasPassCode(context)) {
            binding.textViewPassCodeTitle.setText(R.string.app_passcode_enter_title)
            initNormalUserListener()
            binding.pinView.addTextChangedListener(normalUserListener)
        } else {
            binding.textViewPassCodeTitle.setText(R.string.app_passcode_new_title)
            initFirstTimeListener()
            initNormalUserListener()
            binding.pinView.addTextChangedListener(firstTimeUserListener)
        }
    }

    private fun initFirstTimeListener() {
        firstTimeUserListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 4) {
                    if (tempPassCode.isEmpty()) {
                        tempPassCode = s.toString()
                        binding.textViewPassCodeTitle.setText(R.string.app_passcode_second_new_title)
                        binding.pinView.text?.clear()
                        return
                    }

                    if (tempPassCode == s.toString()) {
                        PassCodeSharedPreferences.updatePassCode(
                            context = context,
                            newPassCode = tempPassCode,
                            isFirstTime = true
                        )
                        tempPassCode = ""
                        binding.textViewPassCodeTitle.setText(R.string.app_passcode_enter_title)
                        binding.pinView.removeTextChangedListener(firstTimeUserListener)
                        binding.pinView.addTextChangedListener(normalUserListener)
                        deactivatePinWidget()
                    } else {
                        tempPassCode = ""
                        binding.textViewPassCodeTitle.setText(R.string.app_passcode_new_title)
                        binding.pinView.text?.clear()
                        showNotMatchPasswordSnackBar()
                    }
                }
            }
        }
    }

    private fun initNormalUserListener() {
        normalUserListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 4) {
                    if (PassCodeSharedPreferences.checkPassCode(context, s.toString())) {
                        deactivatePinWidget()
                    } else {
                        binding.pinView.text?.clear()
                        showWrongPasswordSnackBar()
                    }
                }
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
        hideKeyboard()
        return false
    }

    private fun hideKeyboard() {
        val keyboard: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        keyboard?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun showWrongPasswordSnackBar() {
        hideKeyboard()
        Snackbar.make(this, R.string.app_passcode_invalid_message, Snackbar.LENGTH_LONG)
            .setAction("close") {}
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light, null))
            .show()
    }

    private fun showNotMatchPasswordSnackBar() {
        hideKeyboard()
        Snackbar.make(this, R.string.app_passcode_not_match_message, Snackbar.LENGTH_LONG)
            .setAction("close") { }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light, null))
            .show()
    }
}