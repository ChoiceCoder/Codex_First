package com.govtprep.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val email: String,
    val name: String,
    val streak: Int = 0,
    val xp: Int = 0,
    @SerialName("is_admin") val isAdmin: Boolean = false
)

@Serializable
data class Subject(
    val id: String,
    val name: String,
    val description: String
)

@Serializable
data class Test(
    val id: String,
    @SerialName("subject_id") val subjectId: String,
    val title: String,
    @SerialName("duration_minutes") val durationMinutes: Int,
    @SerialName("total_marks") val totalMarks: Int
)

@Serializable
data class Question(
    val id: String,
    @SerialName("test_id") val testId: String,
    val prompt: String,
    val options: List<String>,
    @SerialName("correct_index") val correctIndex: Int,
    val marks: Int
)

@Serializable
data class Attempt(
    val id: String? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("test_id") val testId: String,
    val score: Int,
    val percentage: Double,
    val percentile: Double,
    val xp: Int,
    @SerialName("completed_at") val completedAt: String
)

@Serializable
data class Answer(
    val id: String? = null,
    @SerialName("attempt_id") val attemptId: String,
    @SerialName("question_id") val questionId: String,
    @SerialName("selected_index") val selectedIndex: Int,
    @SerialName("is_correct") val isCorrect: Boolean
)

@Serializable
data class Achievement(
    val key: String,
    val title: String,
    val unlocked: Boolean
)

data class TestResult(
    val score: Int,
    val percentage: Double,
    val percentile: Double,
    val xpEarned: Int,
    val streak: Int,
    val achievements: List<Achievement>
)
