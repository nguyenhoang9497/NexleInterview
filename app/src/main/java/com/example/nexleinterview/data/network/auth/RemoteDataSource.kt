package com.example.nexleinterview.data.network.auth

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.network.ApiService
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.extension.safeApiCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) :
    DataSource {
    override fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>> =
        safeApiCall {
            apiService.login(loginRequest)
        }
}

