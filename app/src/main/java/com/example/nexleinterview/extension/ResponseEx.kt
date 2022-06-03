package com.example.nexleinterview.extension

import com.example.nexleinterview.data.model.ErrorModel
import com.example.nexleinterview.data.model.response.BaseResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T, R> Response<T>.mapSuccess(
    crossinline block: (T?) -> R
): R {
    val safeBody = body()
    if (this.isSuccessful) {
        return block(safeBody)
    } else {
        throw toError()
    }
}


fun <T> Response<T>.exceptionOnSuccessResponse(): ErrorModel.Http? {
    if (isSuccessful) {
        return null
    }
    return null
}

private val json = Json {
    ignoreUnknownKeys = true
}

fun <T> Response<T>.toError(): ErrorModel.Http {
    try {
        val errorResponse = errorBody()?.string()?.let {
            json.decodeFromString<BaseResponse>(it)
        }
        val detailCode = errorResponse?.statusCode ?: ErrorModel.UNKNOWN_DETAIL_CODE
        val message = ErrorModel.LocalErrorException.UNKNOWN_EXCEPTION.message

        return ErrorModel.Http.ApiError(
            code = detailCode.toString(),
            message = message,
            apiUrl = this.raw().request.url.toString()
        )
    } catch (ex: Exception) {
        return ErrorModel.Http.ApiError(
            code = code().toString(),
            message = ErrorModel.LocalErrorException.UNKNOWN_EXCEPTION.message,
            apiUrl = this.raw().request.url.toString()
        )
    }
}

fun Throwable.toError(): ErrorModel {
    return when (this) {
        is SocketTimeoutException -> ErrorModel.LocalError(
            ErrorModel.LocalErrorException.REQUEST_TIME_OUT_EXCEPTION.message,
            ErrorModel.LocalErrorException.REQUEST_TIME_OUT_EXCEPTION.code
        )
        is UnknownHostException -> ErrorModel.LocalError(
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.message,
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.code
        )
        is ConnectException -> ErrorModel.LocalError(
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.message,
            ErrorModel.LocalErrorException.NO_INTERNET_EXCEPTION.code
        )
        else -> ErrorModel.LocalError(this.message ?: "", "1014")
    }
}
