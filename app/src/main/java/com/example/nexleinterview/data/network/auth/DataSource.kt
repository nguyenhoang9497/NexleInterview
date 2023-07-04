package com.example.nexleinterview.data.network.auth

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.extension.Result
import kotlinx.coroutines.flow.Flow

interface DataSource {
    fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>>
}
