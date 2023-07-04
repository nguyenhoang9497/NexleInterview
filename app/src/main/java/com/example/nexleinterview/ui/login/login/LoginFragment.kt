package com.example.nexleinterview.ui.login.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.loadingFlow
import com.example.nexleinterview.R
import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.databinding.FragmentLoginBinding
import com.example.nexleinterview.extension.collectFlow
import com.example.nexleinterview.extension.onError
import com.example.nexleinterview.extension.onSuccess
import com.example.nexleinterview.ui.MainActivity
import com.example.nexleinterview.ui.base.BaseFragment
import com.example.nexleinterview.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectFlow(viewModel.loadingFlow) {
            handleProgressDialogStatus(it)
        }
        viewModel.event()
    }

    override suspend fun initState() {
        viewModel.stateFlow.collect {
            when(it) {
                LoginViewModel.LoginState.OK -> {

                }
                is LoginViewModel.LoginState.CollectShareFlow -> {
                    binding.edtEmail.setText(it.value.toString())
                }
            }
        }
    }


    override fun initListener() {
        binding.run {
            btnLogin.setOnClickListener {
                tvRequireEmail.isInvisible = viewModel.isValidEmail(edtEmail.text.toString())
                tvRequirePassword.isInvisible =
                    viewModel.isValidPassword(edtPassword.text.toString())
                viewModel.login(
                    LoginRequest(
                        edtEmail.text.toString(),
                        edtPassword.text.toString()
                    )
                ).onSuccess {
                    Log.d("AAAA", "initListener: $it")
                }.onError {
                    Log.d("AAAA", "initListener: $it")
                }
                    .launchIn(lifecycleScope)
                context?.let {
                    it.startActivity(Intent(it, MainActivity::class.java))
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
