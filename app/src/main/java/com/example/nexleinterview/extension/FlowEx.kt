package com.example.nexleinterview.extension

import com.example.nexleinterview.data.model.ErrorModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*

sealed class FlowResult<out T> {
    data class Success<T>(val value: T) : FlowResult<T>()
    data class Error(val error: ErrorModel) : FlowResult<Nothing>()
}

suspend inline fun <T> safeUseCase(
    crossinline block: suspend () -> T,
): FlowResult<T> = try {
    FlowResult.Success(block())
} catch (e: ErrorModel) {
    FlowResult.Error(e.toError())
}

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

fun <T> observableFlow(block: suspend FlowCollector<T>.() -> Unit): Flow<FlowResult<T>> =
    flow(block)
        .catch { exception ->
            FlowResult.Error(exception.toError())
        }
        .map {
            FlowResult.Success(it)
        }

fun <T> Flow<FlowResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Success<T>) {
            action(result.value)
        }
        return@transform emit(result)
    }

fun <T> Flow<FlowResult<T>>.mapSuccess(): Flow<T> =
    transform { result ->
        if (result is FlowResult.Success<T>) {
            emit(result.value)
        }
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

fun interval(period: Long, unit: TimeUnit = MILLISECONDS, initialDelay: Long = 0) = flow<Long> {
    var counter = 0L

    val delayInMillis = convertTimeUnit(initialDelay, unit)
    val periodInMillis = convertTimeUnit(period, unit)

    delay(delayInMillis)
    while (true) {
        delay(periodInMillis)
        emit(counter++)
    }
}

fun convertTimeUnit(timeInMillis: Long, unit: TimeUnit) = when (unit) {
    NANOSECONDS -> timeInMillis / 1_000_000
    MICROSECONDS -> timeInMillis / 1_000
    MILLISECONDS -> timeInMillis
    SECONDS -> timeInMillis * 1_000
    MINUTES -> timeInMillis * 1_000 * 60
    HOURS -> timeInMillis * 1_000 * 60 * 60
    DAYS -> timeInMillis * 1_000 * 60 * 60 * 24
}
