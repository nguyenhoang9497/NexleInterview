package com.example.nexleinterview.ui.login.login

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.extension.FlowResult
import kotlinx.coroutines.flow.Flow

interface LoginVMContract {
    fun isValidEmail(email: String): Boolean

    fun isValidPassword(password: String): Boolean

    fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>>
}
