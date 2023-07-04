package com.example.nexleinterview.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>(private val inflate: (layoutInflater: LayoutInflater, ViewGroup?, Boolean) -> T) :
    Fragment() {
    protected lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initListener()
    }

    abstract fun initData()

    abstract fun initListener()

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
