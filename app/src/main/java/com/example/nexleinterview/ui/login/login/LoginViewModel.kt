package com.example.nexleinterview.ui.login.login

import android.hardware.lights.LightState
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.bindLoading
import androidx.lifecycle.viewModelScope
import com.example.nexleinterview.data.model.request.LoginRequest
import com.example.nexleinterview.data.model.response.LoginResponse
import com.example.nexleinterview.data.network.auth.Repository
import com.example.nexleinterview.di.ShareFlow
import com.example.nexleinterview.extension.FlowResult
import com.example.nexleinterview.ui.login.LoginActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val shareFlow: ShareFlow
) : ViewModel(),
    LoginVMContract {

    private var _stateFlow = MutableStateFlow<LoginState>(LoginState.OK)
    val stateFlow: StateFlow<LoginState> = _stateFlow

    sealed class LoginState {
        object OK: LoginState()

        data class CollectShareFlow(val value: Int): LoginState()
    }

    init {
        viewModelScope.launch {
            shareFlow.events.collect {
                Log.d("AAAA", "$it ")
                _stateFlow.emit(LoginState.CollectShareFlow(it))
            }
        }
    }

    override fun isValidEmail(email: String) =
        (Patterns.EMAIL_ADDRESS.matcher(email).matches())

    override fun isValidPassword(password: String): Boolean =
        (Pattern.compile(LoginActivity.PASSWORD_PATTERN).matcher(password)
            .matches() && password.length in 6..18)

    override fun login(loginRequest: LoginRequest): Flow<FlowResult<LoginResponse>> =
        repository.login(loginRequest)
            .bindLoading(this)

    override fun event() {
        viewModelScope.launch {
            shareFlow.event(1)
        }
    }
}
