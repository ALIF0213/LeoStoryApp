package com.example.leostoryapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    init {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !isValidEmail(s.toString())) {
                    "Format email tidak valid".showError()
                } else {
                    clearError()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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