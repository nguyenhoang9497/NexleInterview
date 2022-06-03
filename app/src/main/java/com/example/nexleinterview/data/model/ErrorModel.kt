package com.example.nexleinterview.data.model

import java.net.HttpURLConnection

sealed class ErrorModel(open var isBackOnError: Boolean = false) : Throwable() {

    companion object {
        const val UNKNOWN_DETAIL_CODE = "10101"
    }

    open fun isCommonError(): Boolean = false

    sealed class Http(isBackOnError: Boolean = false) : ErrorModel(isBackOnError) {
        data class ApiError(
            val code: String?,
            override val message: String?,
            val apiUrl: String?,
            override var isBackOnError: Boolean = false
        ) : Http(isBackOnError) {
            override fun isCommonError(): Boolean {
                if (code == HttpURLConnection.HTTP_UNAUTHORIZED.toString()
                    || code == HttpURLConnection.HTTP_INTERNAL_ERROR.toString()
                    || code == HttpURLConnection.HTTP_BAD_REQUEST.toString()
                    || code == HttpURLConnection.HTTP_NOT_FOUND.toString()
                    || code == HttpURLConnection.HTTP_UNAVAILABLE.toString()
                ) {
                    return true
                }
                return false
            }
        }
    }

    data class LocalError(
        val errorMessage: String,
        val code: String?,
        override var isBackOnError: Boolean = false
    ) : ErrorModel(isBackOnError)

    enum class LocalErrorException(val message: String, val code: String) {
        NO_INTERNET_EXCEPTION("No Internet", "1001"),
        REQUEST_TIME_OUT_EXCEPTION("Request timeout", "1002"),
        UNKNOWN_EXCEPTION("Unknown Error", "1000")
    }
}
