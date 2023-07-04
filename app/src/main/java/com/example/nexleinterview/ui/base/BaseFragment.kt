package com.example.nexleinterview.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.nexleinterview.ui.dialog.CustomProgressDialog
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewBinding>(private val inflate: (layoutInflater: LayoutInflater, ViewGroup?, Boolean) -> T) :
    Fragment() {
    protected lateinit var binding: T

    private val progressDialog by lazy {
        CustomProgressDialog.newInstance()
    }

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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                initState()
            }
        }
        initListener()
    }

    abstract suspend fun initState()

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

    internal fun handleProgressDialogStatus(isShow: Boolean) {
        if (isShow) {
            progressDialog.show(
                childFragmentManager,
                CustomProgressDialog::class.java.simpleName
            )
        } else {
            progressDialog.dismissAllowingStateLoss()
        }
    }
}
