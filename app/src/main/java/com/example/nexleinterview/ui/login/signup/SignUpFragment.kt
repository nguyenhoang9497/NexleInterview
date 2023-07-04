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
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignupBinding>(FragmentSignupBinding::inflate) {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private val viewModel: SignUpViewModel by viewModels()

    override suspend fun initState() {
        viewModel.stateFlow.collect {
            when (it) {
                SignUpViewModel.SignUpState.OK -> {}
                is SignUpViewModel.SignUpState.PasswordStrengthError -> {
                    binding.tvRequirePassword.text = requireContext().getString(it.strength.stringResource)
                    binding.tvRequirePassword.setTextColor(
                        resources.getColor(
                            it.strength.colorResource,
                            (activity as? LoginActivity)?.theme
                        )
                    )
                }
            }
        }
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
                viewModel.event()
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
                    viewModel.passwordStrength(text.toString())
                }
            }
        }
    }
}
