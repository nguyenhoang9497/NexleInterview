package com.example.nexleinterview.ui.login.signup

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.nexleinterview.R
import com.example.nexleinterview.databinding.FragmentSignupBinding
import com.example.nexleinterview.ui.base.BaseFragment
import com.example.nexleinterview.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val viewModel: SignUpVMContract by viewModels()

    override fun initData() {

    }

    override fun initListener() {
        binding.run {
            btnSignup.setOnClickListener {
                tvRequireEmail.isInvisible = viewModel.isValidEmail(edtEmail.text.toString())
                tvRequireFirstName.isInvisible =
                    viewModel.isValidFirstName(edtFirstName.text.toString())
                tvRequireLastName.isInvisible =
                    viewModel.isValidLastName(edtLastName.text.toString())
                tvRequirePassword.isInvisible =
                    viewModel.isValidPassword(edtPassword.text.toString())
                (activity as? LoginActivity)?.popFragment()
            }

            tvAlreadyHaveAccount.setOnClickListener {
                (activity as? LoginActivity)?.popFragment()
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
