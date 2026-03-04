package com.govtprep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.govtprep.data.model.UserProfile
import com.govtprep.data.repository.AuthRepository
import com.govtprep.data.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val testRepository: TestRepository
) : ViewModel() {
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _state.value = HomeUiState.Loading
            val profile = authRepository.currentProfile().getOrNull()
            if (profile == null) {
                _state.value = HomeUiState.Error("Profile missing")
                return@launch
            }
            val subjects = testRepository.subjects().getOrElse {
                _state.value = HomeUiState.Error(it.message ?: "Subjects load failed")
                return@launch
            }
            val tests = testRepository.tests().getOrElse {
                _state.value = HomeUiState.Error(it.message ?: "Tests load failed")
                return@launch
            }
            _state.value = HomeUiState.Ready(
                profile = profile,
                subjects = subjects,
                tests = tests,
                trend = tests.indices.map { ((it + 1) * 10).toFloat() }
            )
        }
    }

    fun profileOrNull(): UserProfile? = (state.value as? HomeUiState.Ready)?.profile
}
