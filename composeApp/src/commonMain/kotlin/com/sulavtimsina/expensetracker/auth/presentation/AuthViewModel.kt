package com.sulavtimsina.expensetracker.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sulavtimsina.expensetracker.auth.data.AuthRepository
import com.sulavtimsina.expensetracker.core.domain.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId = _currentUserId.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            authRepository.isAuthenticated.collect { authenticated ->
                _isAuthenticated.value = authenticated
            }
        }

        viewModelScope.launch {
            authRepository.currentUser.collect { userId ->
                _currentUserId.value = userId
            }
        }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signIn(email, password)
            when (result) {
                is Result.Success -> {
                    isLoading = false
                    onSuccess()
                }
                is Result.Error -> {
                    isLoading = false
                    errorMessage = result.error.name
                }
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signUp(email, password)
            when (result) {
                is Result.Success -> {
                    isLoading = false
                    onSuccess()
                }
                is Result.Error -> {
                    isLoading = false
                    errorMessage = result.error.name
                }
            }
        }
    }

    fun signInAnonymously(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = authRepository.signInAnonymously()
            when (result) {
                is Result.Success -> {
                    isLoading = false
                    onSuccess()
                }
                is Result.Error -> {
                    isLoading = false
                    errorMessage = result.error.name
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun clearError() {
        errorMessage = null
    }
}
