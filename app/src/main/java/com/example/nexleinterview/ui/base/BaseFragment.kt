package com.example.nexleinterview.ui.base

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {
    internal fun handleEditText(
        editText: EditText,
        notValid: Int,
        require: Int,
        errorTextView: TextView,
        isValid: Boolean
    ) {
        editText.doAfterTextChanged {
            if (!isValid) {
                errorTextView.visibility = View.VISIBLE
                if (editText.text.toString().isEmpty()) {
                    errorTextView.text = requireContext().getString(require)
                } else {
                    errorTextView.text = requireContext().getString(notValid)
                }
            } else {
                errorTextView.visibility = View.INVISIBLE
            }
        }
    }

}
