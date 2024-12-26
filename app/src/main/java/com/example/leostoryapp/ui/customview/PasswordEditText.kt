package com.example.leostoryapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    init {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8) {
                    "Password harus minimal 8 karakter".showError()
                } else {
                    clearError()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun String.showError() {
        val parentLayout = parent.parent as? TextInputLayout
        parentLayout?.error = this
    }

    private fun clearError() {
        val parentLayout = parent.parent as? TextInputLayout
        parentLayout?.error = null
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }
}
