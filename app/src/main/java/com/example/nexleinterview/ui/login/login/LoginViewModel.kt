package com.example.nexleinterview.ui.login.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.network.auth.AuthRepository
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.ui.login.LoginActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel(), LoginVMContract {
    override fun isValidEmail(email: String) =
        (Patterns.EMAIL_ADDRESS.matcher(email).matches())

    override fun isValidPassword(password: String): Boolean =
        (Pattern.compile(LoginActivity.PASSWORD_PATTERN).matcher(password).matches() && password.length in 6..18)

    override fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>> =
        authRepository.login(loginRequest)
}
