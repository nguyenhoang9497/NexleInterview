package com.example.nexleinterview.data.network.auth

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.data.model.response.BaseResponse
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.model.response.MoviePopularResponse
import com.example.nexleinterview.data.model.response.SignupResponse
import com.example.nexleinterview.extension.FlowResult
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>>

    fun signup(signupRequest: SignupRequest): Flow<FlowResult<SignupResponse>>

    fun logout(): Flow<FlowResult<BaseResponse>>
}
