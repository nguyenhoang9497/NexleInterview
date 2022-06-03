package com.example.nexleinterview.ui

import com.example.nexleinterview.data.model.response.BaseResponse
import com.example.nexleinterview.extension.FlowResult
import kotlinx.coroutines.flow.Flow

interface MainVMContract {
    fun logout(): Flow<FlowResult<BaseResponse>>
}
