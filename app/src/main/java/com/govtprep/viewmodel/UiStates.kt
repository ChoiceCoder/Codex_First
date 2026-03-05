package com.govtprep.viewmodel

import com.govtprep.data.model.Question
import com.govtprep.data.model.Subject
import com.govtprep.data.model.Test
import com.govtprep.data.model.TestResult
import com.govtprep.data.model.UserProfile

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: UserProfile?) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Ready(
        val profile: UserProfile,
        val subjects: List<Subject>,
        val tests: List<Test>,
        val trend: List<Float>
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}

sealed interface ExamUiState {
    data object Idle : ExamUiState
    data object Loading : ExamUiState
    data class Running(
        val test: Test,
        val questions: List<Question>,
        val index: Int,
        val remainingSeconds: Int,
        val selections: Map<String, Int>
    ) : ExamUiState

    data class Submitted(val result: TestResult) : ExamUiState
    data class Error(val message: String) : ExamUiState
}
