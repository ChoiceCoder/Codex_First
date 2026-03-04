package com.govtprep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.govtprep.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun hasSession(): Boolean = authRepository.hasSession()
    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun checkSession() {
        if (authRepository.hasSession()) {
            viewModelScope.launch {
                _state.value = authRepository.currentProfile().fold(
                    onSuccess = { AuthUiState.Success(it) },
                    onFailure = { AuthUiState.Error(it.message ?: "Unknown error") }
                )
            }
        } else {
            _state.value = AuthUiState.Error("No active session")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthUiState.Loading
            _state.value = authRepository.login(email, password).fold(
                onSuccess = {
                    authRepository.currentProfile().fold(
                        onSuccess = { AuthUiState.Success(it) },
                        onFailure = { AuthUiState.Error(it.message ?: "Profile load failed") }
                    )
                },
                onFailure = { AuthUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthUiState.Loading
            _state.value = authRepository.signUp(name, email, password).fold(
                onSuccess = {
                    authRepository.currentProfile().fold(
                        onSuccess = { AuthUiState.Success(it) },
                        onFailure = { AuthUiState.Error(it.message ?: "Profile load failed") }
                    )
                },
                onFailure = { AuthUiState.Error(it.message ?: "Signup failed") }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = AuthUiState.Idle
        }
    }
}
