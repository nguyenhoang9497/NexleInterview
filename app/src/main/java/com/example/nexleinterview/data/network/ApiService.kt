package com.example.nexleinterview.data.network

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("authentication")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

}
