package com.govtprep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.govtprep.data.model.Test
import com.govtprep.data.repository.AuthRepository
import com.govtprep.data.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val testRepository: TestRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ExamUiState>(ExamUiState.Idle)
    val state: StateFlow<ExamUiState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun startExam(test: Test) {
        viewModelScope.launch {
            _state.value = ExamUiState.Loading
            val questions = testRepository.shuffledQuestions(test.id).getOrElse {
                _state.value = ExamUiState.Error(it.message ?: "Question load failed")
                return@launch
            }
            _state.value = ExamUiState.Running(
                test = test,
                questions = questions,
                index = 0,
                remainingSeconds = test.durationMinutes * 60,
                selections = emptyMap()
            )
            startTimer()
        }
    }

    fun selectAnswer(questionId: String, selectedIndex: Int) {
        val current = _state.value as? ExamUiState.Running ?: return
        _state.value = current.copy(selections = current.selections + (questionId to selectedIndex))
    }

    fun next() {
        val current = _state.value as? ExamUiState.Running ?: return
        if (current.index < current.questions.lastIndex) {
            _state.value = current.copy(index = current.index + 1)
        }
    }

    fun previous() {
        val current = _state.value as? ExamUiState.Running ?: return
        if (current.index > 0) {
            _state.value = current.copy(index = current.index - 1)
        }
    }

    fun submit() {
        timerJob?.cancel()
        val current = _state.value as? ExamUiState.Running ?: return
        viewModelScope.launch {
            val user = authRepository.currentProfile().getOrNull()
            if (user == null) {
                _state.value = ExamUiState.Error("User missing")
                return@launch
            }
            _state.value = testRepository.submitAttempt(
                userId = user.id,
                test = current.test,
                questions = current.questions,
                answersByQuestion = current.selections
            ).fold(
                onSuccess = { ExamUiState.Submitted(it) },
                onFailure = { ExamUiState.Error(it.message ?: "Submit failed") }
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val running = _state.value as? ExamUiState.Running ?: break
                val left = running.remainingSeconds - 1
                if (left <= 0) {
                    submit()
                    break
                }
                _state.value = running.copy(remainingSeconds = left)
            }
        }
    }
}
