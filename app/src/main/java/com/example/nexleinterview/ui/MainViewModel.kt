package com.example.nexleinterview.ui

import androidx.lifecycle.ViewModel
import com.example.nexleinterview.data.network.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(),
    MainVMContract {
    override fun logout() =
        authRepository.logout()
}
