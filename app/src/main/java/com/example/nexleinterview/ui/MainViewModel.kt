package com.example.nexleinterview.ui

import androidx.lifecycle.ViewModel
import com.example.nexleinterview.data.network.auth.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    MainVMContract {

    val stateFLow: StateFlow<MainState> = MutableStateFlow(MainState.OK)

    sealed class MainState {
        object OK: MainState()
    }
}
