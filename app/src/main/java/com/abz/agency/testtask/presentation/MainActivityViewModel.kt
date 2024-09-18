package com.abz.agency.testtask.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abz.agency.testtask.core.ConnectionState
import com.abz.agency.testtask.domain.usecases.GetConnectionStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getConnectionStateUseCase: GetConnectionStateUseCase
): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // get network status
    val networkStatus: StateFlow<ConnectionState> = getConnectionStateUseCase().stateIn(
        initialValue = ConnectionState.Unknown,
        scope = viewModelScope,
        started = WhileSubscribed(STOP_DELAY),
    )

    init {
        viewModelScope.launch {
            // delay to show splash screen
            delay(SPLASH_DELAY)
            _isLoading.value = false
        }
    }

    companion object {
        private const val SPLASH_DELAY = 1000L
        private const val STOP_DELAY = 3000L
    }
}