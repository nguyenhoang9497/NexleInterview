package com.example.nexleinterview.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    @SerialName("firstName") val firstName: String? = "",
    @SerialName("lastName") val lastName: String? = "",
    @SerialName("email") val email: String? = "",
    @SerialName("password") val password: String? = ""
)
