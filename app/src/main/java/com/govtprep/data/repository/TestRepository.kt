package com.govtprep.data.repository

import com.govtprep.data.model.Achievement
import com.govtprep.data.model.Answer
import com.govtprep.data.model.Attempt
import com.govtprep.data.model.Question
import com.govtprep.data.model.Subject
import com.govtprep.data.model.Test
import com.govtprep.data.model.TestResult
import com.govtprep.data.remote.SupabaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

class TestRepository @Inject constructor(
    private val dataSource: SupabaseDataSource
) {
    suspend fun subjects(): Result<List<Subject>> = withContext(Dispatchers.IO) { runCatching { dataSource.fetchSubjects() } }
    suspend fun tests(): Result<List<Test>> = withContext(Dispatchers.IO) { runCatching { dataSource.fetchTests() } }

    suspend fun shuffledQuestions(testId: String): Result<List<Question>> = withContext(Dispatchers.IO) {
        runCatching { dataSource.fetchQuestions(testId).shuffled() }
    }

    suspend fun submitAttempt(
        userId: String,
        test: Test,
        questions: List<Question>,
        answersByQuestion: Map<String, Int>
    ): Result<TestResult> = withContext(Dispatchers.IO) {
        runCatching {
            val score = questions.sumOf { q ->
                if (answersByQuestion[q.id] == q.correctIndex) q.marks else 0
            }
            val percentage = if (test.totalMarks == 0) 0.0 else (score.toDouble() / test.totalMarks.toDouble()) * 100.0
            val attempts = dataSource.fetchUserAttempts(userId)
            val percentile = calculatePercentile(percentage, attempts.map { it.percentage })
            val xpEarned = (percentage * 1.2).roundToInt().coerceAtLeast(10)

            val profile = dataSource.fetchUserProfile(userId) ?: error("User profile not found")
            val streak = calculateStreak(profile.streak)
            val totalXp = profile.xp + xpEarned

            val savedAttempt = dataSource.saveAttempt(
                Attempt(
                    userId = userId,
                    testId = test.id,
                    score = score,
                    percentage = percentage,
                    percentile = percentile,
                    xp = xpEarned,
                    completedAt = OffsetDateTime.now().toString()
                )
            )
            val attemptId = savedAttempt.id ?: error("Attempt ID missing")
            dataSource.saveAnswers(
                questions.map { q ->
                    val selected = answersByQuestion[q.id] ?: -1
                    Answer(
                        attemptId = attemptId,
                        questionId = q.id,
                        selectedIndex = selected,
                        isCorrect = selected == q.correctIndex
                    )
                }
            )
            dataSource.updateUserProgress(userId, streak, totalXp)

            TestResult(
                score = score,
                percentage = percentage,
                percentile = percentile,
                xpEarned = xpEarned,
                streak = streak,
                achievements = achievements(streak, totalXp, percentile)
            )
        }
    }

    private fun calculatePercentile(latestPercentage: Double, allPercentages: List<Double>): Double {
        if (allPercentages.isEmpty()) return 95.0
        val below = allPercentages.count { it <= latestPercentage }
        return (below.toDouble() / allPercentages.size.toDouble()) * 100.0
    }

    private fun calculateStreak(currentStreak: Int): Int {
        val today = LocalDate.now().dayOfYear
        return if (today > 0) currentStreak + 1 else currentStreak
    }

    private fun achievements(streak: Int, xp: Int, percentile: Double): List<Achievement> = listOf(
        Achievement("streak_7", "7 Day Warrior", streak >= 7),
        Achievement("xp_500", "500 XP Elite", xp >= 500),
        Achievement("pct_90", "Top 10%", percentile >= 90.0)
    )
}
