package com.example.nexleinterview.ui.login.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.nexleinterview.R
import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.databinding.FragmentSignupBinding
import com.example.nexleinterview.extension.onError
import com.example.nexleinterview.extension.onSuccess
import com.example.nexleinterview.ui.base.BaseFragment
import com.example.nexleinterview.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignupBinding

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.run {
            btnSignup.setOnClickListener {
                tvRequireEmail.isInvisible = viewModel.isValidEmail(edtEmail.text.toString())
                tvRequireFirstName.isInvisible =
                    viewModel.isValidFirstName(edtFirstName.text.toString())
                tvRequireLastName.isInvisible =
                    viewModel.isValidLastName(edtLastName.text.toString())
                tvRequirePassword.isInvisible =
                    viewModel.isValidPassword(edtPassword.text.toString())
                if (viewModel.isValidEmail(edtEmail.text.toString())
                    && viewModel.isValidFirstName(edtFirstName.text.toString())
                    && viewModel.isValidLastName(edtLastName.text.toString())
                    && viewModel.isValidPassword(edtPassword.text.toString())
                    && chkPolicy.isChecked
                ) {
                    val signupRequest = SignupRequest(
                        firstName = edtFirstName.text.toString(),
                        lastName = edtLastName.text.toString(),
                        email = edtEmail.text.toString(),
                        password = edtPassword.text.toString()
                    )
                    viewModel.signup(signupRequest)
                        .onSuccess { response ->
                            Log.d("AAAA", "onSuccess:")
                            Log.d("AAAA", "onSuccess: $response")
                            (activity as? LoginActivity)?.finish()
                        }
                        .onError {
                            Log.d("AAAA", "onError: ${it.message}")
                        }.launchIn(lifecycleScope)
                }
            }

            tvAlreadyHaveAccount.setOnClickListener {
                (activity as? LoginActivity)?.onBackPressed()
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

            edtFirstName.doOnTextChanged { text, _, _, _ ->
                handleEditText(
                    edtFirstName,
                    R.string.signup_not_valid_first_name,
                    R.string.signup_require_first_name,
                    tvRequireFirstName,
                    viewModel.isValidFirstName(text.toString())
                )
            }

            edtLastName.doOnTextChanged { text, _, _, _ ->
                handleEditText(
                    edtLastName,
                    R.string.signup_not_valid_last_name,
                    R.string.signup_require_last_name,
                    tvRequireLastName,
                    viewModel.isValidLastName(text.toString())
                )
            }

            edtPassword.doOnTextChanged { text, _, _, _ ->
                if (!viewModel.isValidPassword(text.toString())) {
                    tvRequirePassword.visibility = View.VISIBLE
                    if (text.toString().isEmpty()) {
                        tvRequirePassword.text =
                            requireContext().getString(R.string.signup_require_password)
                        tvRequirePassword.setTextColor(
                            resources.getColor(
                                R.color.text_error,
                                (activity as? LoginActivity)?.theme
                            )
                        )
                    } else {
                        tvRequirePassword.setTextColor(
                            resources.getColor(
                                R.color.text_error,
                                (activity as? LoginActivity)?.theme
                            )
                        )
                        tvRequirePassword.text =
                            requireContext().getString(R.string.signup_not_valid_password)
                    }
                } else {
                    tvRequirePassword.text =
                        requireContext().getString(viewModel.passwordStrength(text.toString()))
                    when (viewModel.passwordStrength(text.toString())) {
                        SignUpViewModel.PasswordStrength.WEAK.value -> {
                            tvRequirePassword.setTextColor(
                                resources.getColor(
                                    R.color.weak_password,
                                    (activity as? LoginActivity)?.theme
                                )
                            )
                        }
                        SignUpViewModel.PasswordStrength.FAIR.value -> {
                            tvRequirePassword.setTextColor(
                                resources.getColor(
                                    R.color.fair_password,
                                    (activity as? LoginActivity)?.theme
                                )
                            )
                        }
                        SignUpViewModel.PasswordStrength.GOOD.value -> {
                            tvRequirePassword.setTextColor(
                                resources.getColor(
                                    R.color.good_password,
                                    (activity as? LoginActivity)?.theme
                                )
                            )
                        }
                        SignUpViewModel.PasswordStrength.STRONG.value -> {
                            tvRequirePassword.setTextColor(
                                resources.getColor(
                                    R.color.strong_password,
                                    (activity as? LoginActivity)?.theme
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
