@file:Suppress("PackageDirectoryMismatch")
package androidx.lifecycle

import com.example.nexleinterview.data.model.ErrorModel
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.extension.onError
import com.example.nexleinterview.extension.onSuccess
import kotlinx.coroutines.flow.*

private const val LOADING_FLOW_KEY = "androidx.lifecycle.LoadingFlow"

val <T> T.loadingFlow: StateFlow<Boolean> where T : ViewModel
    get() {
        return loadingMutableStateFlow
    }

private val <T> T.loadingMutableStateFlow: MutableStateFlow<Boolean> where T : ViewModel
    get() {
        val flow: MutableStateFlow<Boolean>? = getTag(LOADING_FLOW_KEY)
        return flow ?: setTagIfAbsent(LOADING_FLOW_KEY, MutableStateFlow(false))
    }

fun <F, T> Flow<FlowResult<F>>.bindLoading(t: T): Flow<FlowResult<F>> where T : ViewModel {
    return this
        .onStart {
            t.loadingMutableStateFlow.value = true
        }
        .onSuccess {
            t.loadingMutableStateFlow.value = false
        }.onError({ t.loadingMutableStateFlow.value = false },
            { t.loadingMutableStateFlow.value = false })
}