package com.example.nexleinterview.ui.login.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.nexleinterview.R
import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.databinding.FragmentLoginBinding
import com.example.nexleinterview.extension.onSuccess
import com.example.nexleinterview.ui.MainActivity
import com.example.nexleinterview.ui.base.BaseFragment
import com.example.nexleinterview.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.run {
            btnLogin.setOnClickListener {
                tvRequireEmail.isInvisible = viewModel.isValidEmail(edtEmail.text.toString())
                tvRequirePassword.isInvisible =
                    viewModel.isValidPassword(edtPassword.text.toString())
                if (viewModel.isValidPassword(edtPassword.text.toString())
                    && viewModel.isValidEmail(edtEmail.text.toString())
                ) {
                    context?.let {
                        it.startActivity(Intent(it, MainActivity::class.java))
                    }
                    viewModel.login(
                        LoginRequest(
                            edtEmail.text.toString(),
                            edtPassword.text.toString()
                        )
                    ).onSuccess {
                        context?.let {
                            it.startActivity(Intent(it, MainActivity::class.java))
                        }
                    }.launchIn(lifecycleScope)
                }
            }
            tvCreateAccount.setOnClickListener {
                (activity as? LoginActivity)?.openSignupFragment()
            }
            edtEmail.doOnTextChanged { text, _, _, _ ->
                handleEditText(
                    edtEmail,
                    R.string.signup_not_valid_email,
                    R.string.signup_require_email,
                    tvRequireEmail,
                    viewModel.isValidEmail(text.toString())
                )
            }

            edtPassword.doOnTextChanged { text, _, _, _ ->
                handleEditText(
                    edtEmail,
                    R.string.signup_not_valid_email,
                    R.string.signup_require_email,
                    tvRequireEmail,
                    viewModel.isValidPassword(text.toString())
                )
            }
        }
    }
}
