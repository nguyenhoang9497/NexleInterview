package com.example.nexleinterview.ui.login.signup

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.nexleinterview.R
import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.data.model.response.MoviePopularResponse
import com.example.nexleinterview.data.model.response.SignupResponse
import com.example.nexleinterview.data.network.auth.AuthRepository
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.extension.onSuccess
import com.example.nexleinterview.ui.login.LoginActivity.Companion.PASSWORD_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(),
    SignUpVMContract {


    override fun signup(signupRequest: SignupRequest): Flow<FlowResult<SignupResponse>> =
        authRepository.signup(signupRequest)

    override fun isValidFirstName(name: String) =
        name.length >= 2

    override fun isValidLastName(name: String): Boolean =
        name.length >= 2

    override fun isValidEmail(email: String) =
        (Patterns.EMAIL_ADDRESS.matcher(email).matches())

    override fun isValidPassword(password: String): Boolean =
        (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches() && password.length in 6..18)

    override fun passwordStrength(password: String): Int {
        return when {
            (password.length <= 3) -> {
                PasswordStrength.WEAK.value
            }
            (password.length in 4..6) -> {
                PasswordStrength.FAIR.value
            }
            (password.length in 7..9) -> {
                PasswordStrength.GOOD.value
            }
            else -> {
                PasswordStrength.STRONG.value
            }
        }
    }

    enum class PasswordStrength(val value: Int) {
        WEAK(R.string.weak_password),
        FAIR(R.string.fair_password),
        GOOD(R.string.good_password),
        STRONG(R.string.strong_password)
    }
}
