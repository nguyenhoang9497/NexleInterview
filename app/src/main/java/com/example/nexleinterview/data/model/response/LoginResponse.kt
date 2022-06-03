package com.example.nexleinterview.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("_id") val _id: String? = "",
    @SerialName("email") val email: String? = "",
    @SerialName("admin") val admin: Boolean? = false,
    @SerialName("firstName") val firstName: String? = "",
    @SerialName("lastName") val lastName: String? = "",
    @SerialName("createdAt") val createdAt: String? = "",
    @SerialName("updatedAt") val updatedAt: String? = "",
    @SerialName("_V") val _V: Int? = -1,
    @SerialName("displayName") val displayName: String? = "",
    @SerialName("token") val token: String? = "",
    @SerialName("refreshToken") val refreshToken: String? = "",
)
