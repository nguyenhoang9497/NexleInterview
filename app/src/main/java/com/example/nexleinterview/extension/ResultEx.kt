package com.example.nexleinterview.extension

import com.example.nexleinterview.data.model.ErrorModel
import com.example.nexleinterview.data.model.response.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class FlowResult<out T> {
    data class Success<T>(val value: T) : FlowResult<T>()
    data class Error(val error: ErrorModel) : FlowResult<Nothing>()
}

inline fun <T> safeApiCall(crossinline block: suspend () -> Response<T>) =
    safeFlow { suspendApiCall(block) }

inline fun <T> safeFlow(
    crossinline block: suspend () -> T,
): Flow<FlowResult<T>> = flow {
    try {
        val repoResult = block()
        emit(FlowResult.Success(repoResult))
    } catch (e: ErrorModel) {
        emit(FlowResult.Error(e))
    } catch (e: Exception) {
        emit(FlowResult.Error(e.toError()))
    }
}

fun <T> Flow<FlowResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Success<T>) {
            action(result.value)
        }
        return@transform emit(result)
    }

fun <T> Flow<FlowResult<T>>.onError(
    action: suspend (ErrorModel) -> Unit = {},
    commonAction: suspend (ErrorModel) -> Unit = {}
): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Error) {
            if (!result.error.isCommonError()) {
                action(result.error)
            } else {
                commonAction(result.error)
            }
        }
        return@transform emit(result)
    }

suspend inline fun <T> suspendApiCall(
    crossinline block: suspend () -> Response<T>
): T {
    val response = block()
    val body = response.body()
    return when (response.isSuccessful && body != null) {
        true -> body
        false -> throw response.toError()
    }
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
            code = code().toString(),
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
