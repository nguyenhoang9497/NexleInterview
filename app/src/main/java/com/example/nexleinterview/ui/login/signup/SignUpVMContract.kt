package com.example.nexleinterview.ui.login.signup

import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.data.model.response.MoviePopularResponse
import com.example.nexleinterview.data.model.response.SignupResponse
import com.example.nexleinterview.extension.FlowResult
import kotlinx.coroutines.flow.Flow


interface SignUpVMContract {
    fun signup(signupRequest: SignupRequest): Flow<FlowResult<SignupResponse>>

    fun isValidFirstName(name: String): Boolean

    fun isValidLastName(name: String): Boolean

    fun isValidEmail(email: String): Boolean

    fun isValidPassword(password: String): Boolean

    fun passwordStrength(password: String): Int
}
