package com.example.nexleinterview.data.network.auth

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.data.model.response.BaseResponse
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.model.response.MoviePopularResponse
import com.example.nexleinterview.data.model.response.SignupResponse
import com.example.nexleinterview.extension.FlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource) :
    AuthDataSource {
    override fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>> =
        authRemoteDataSource.login(loginRequest)

    override fun signup(signupRequest: SignupRequest): Flow<FlowResult<SignupResponse>> =
        authRemoteDataSource.signup(signupRequest)

    override fun logout(): Flow<FlowResult<BaseResponse>> =
        authRemoteDataSource.logout()
}
