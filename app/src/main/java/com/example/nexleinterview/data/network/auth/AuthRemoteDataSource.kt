package com.example.nexleinterview.data.network.auth

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.data.model.response.BaseResponse
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.model.response.MoviePopularResponse
import com.example.nexleinterview.data.model.response.SignupResponse
import com.example.nexleinterview.data.network.ApiService
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.extension.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(private val apiService: ApiService) :
    AuthDataSource {
    override fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>> = safeApiCall {
        apiService.login(loginRequest)
    }

    override fun signup(signupRequest: SignupRequest): Flow<FlowResult<SignupResponse>> =
        safeApiCall {
            apiService.signup(signupRequest)
        }

    override fun logout(): Flow<FlowResult<BaseResponse>> = safeApiCall {
        apiService.logout()
    }
}
