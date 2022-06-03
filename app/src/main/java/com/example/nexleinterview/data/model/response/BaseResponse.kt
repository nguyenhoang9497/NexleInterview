package com.example.nexleinterview.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    @SerialName("statusCode") val statusCode: Int? = -1
)
