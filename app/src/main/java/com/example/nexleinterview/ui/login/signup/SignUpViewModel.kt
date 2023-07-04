package com.example.nexleinterview.ui.login.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexleinterview.R
import com.example.nexleinterview.di.ShareFlow
import com.example.nexleinterview.ui.login.LoginActivity.Companion.PASSWORD_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val shareFlow: ShareFlow) : ViewModel(),
    SignUpVMContract {

    private val _stateFlow = MutableStateFlow<SignUpState>(SignUpState.OK)
    val stateFlow: StateFlow<SignUpState> = _stateFlow

    sealed class SignUpState {
        object OK : SignUpState()

        data class PasswordStrengthError(val strength: PasswordStrength) : SignUpState()
    }

    override fun isValidFirstName(name: String) =
        name.length >= 2

    override fun isValidLastName(name: String): Boolean =
        name.length >= 2

    override fun isValidEmail(email: String) =
        (Patterns.EMAIL_ADDRESS.matcher(email).matches())

    override fun isValidPassword(password: String): Boolean =
        (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches() && password.length in 6..18)

    override fun passwordStrength(password: String) {
        val strength = when {
            (password.length <= 3) -> {
                PasswordStrength.WEAK
            }
            (password.length in 4..6) -> {
                PasswordStrength.FAIR
            }
            (password.length in 7..9) -> {
                PasswordStrength.GOOD
            }
            else -> {
                PasswordStrength.STRONG
            }
        }
        viewModelScope.launch {
            _stateFlow.emit(SignUpState.PasswordStrengthError(strength))
        }
    }

    override fun event() {
        viewModelScope.launch {
            shareFlow.event(2)
        }
    }

    enum class PasswordStrength(val stringResource: Int, val colorResource: Int) {
        WEAK(R.string.weak_password, R.color.weak_password),
        FAIR(R.string.fair_password, R.color.fair_password),
        GOOD(R.string.good_password, R.color.good_password),
        STRONG(R.string.strong_password, R.color.strong_password)
    }
}
