package com.example.nexleinterview.data.network

import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.request.SignupRequest
import com.example.nexleinterview.data.model.response.BaseResponse
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.model.response.MoviePopularResponse
import com.example.nexleinterview.data.model.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/signin")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<SignupResponse>

    @GET("auth/logout")
    suspend fun logout(): Response<BaseResponse>
}
