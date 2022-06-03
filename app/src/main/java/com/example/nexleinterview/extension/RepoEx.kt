package com.example.nexleinterview.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

inline fun <T> apiCall(
    block: () -> Response<T>
): T {
    val response = block()
    val body = response.body()
    return when (response.isSuccessful && body != null) {
        true -> body
        false -> throw response.toError()
    }
}

inline fun <T> nullableApiCall(
    block: () -> Response<T>
): T? {
    val response = block()
    val body = response.body()
    return when (response.isSuccessful) {
        true -> body
        false -> throw response.toError()
    }
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

fun <T> T.toSafeFlow() = safeFlow { this }

inline fun <T> safeApiCall(crossinline block: suspend () -> Response<T>) =
    safeFlow { suspendApiCall(block) }

fun <T, R> Flow<FlowResult<T>>.mapIfSuccess(block: (T) -> R): Flow<FlowResult<R>> = this.map {
    if (it is FlowResult.Success) {
        FlowResult.Success(block(it.value))
    } else {
        it as FlowResult.Error
    }
}
