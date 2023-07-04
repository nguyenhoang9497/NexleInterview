package com.example.nexleinterview.di

import kotlinx.coroutines.flow.MutableSharedFlow

class ShareFlow {
    val events = MutableSharedFlow<Int>()
    suspend fun event(event: Int) {
        events.emit(event)
    }
}
