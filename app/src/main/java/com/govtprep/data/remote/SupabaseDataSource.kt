package com.govtprep.data.remote

import com.govtprep.data.model.Answer
import com.govtprep.data.model.Attempt
import com.govtprep.data.model.Question
import com.govtprep.data.model.Subject
import com.govtprep.data.model.Test
import com.govtprep.data.model.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SupabaseDataSource @Inject constructor() {
    private fun client(): SupabaseClient =
        SupabaseClientProvider.clientOrNull()
            ?: error("Supabase is not configured. Set valid SUPABASE_URL and SUPABASE_ANON_KEY.")

    suspend fun signUp(email: String, password: String) =
        client().auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

    suspend fun login(email: String, password: String) =
        client().auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

    suspend fun logout() = client().auth.signOut()

    fun currentUserId(): String? = SupabaseClientProvider.clientOrNull()?.auth?.currentUserOrNull()?.id

    suspend fun fetchUserProfile(userId: String): UserProfile? =
        client().from("users").select {
            filter {
                eq("id", userId)
            }
        }.decodeSingleOrNull<UserProfile>()

    suspend fun upsertUserProfile(profile: UserProfile) {
        client().from("users").upsert(profile)
    }

    suspend fun fetchSubjects(): List<Subject> = client().from("subjects").select().decodeList()

    suspend fun fetchTests(): List<Test> = client().from("tests").select().decodeList()

    suspend fun fetchQuestions(testId: String): List<Question> =
        client().from("questions").select {
            filter {
                eq("test_id", testId)
            }
        }.decodeList()

    suspend fun saveAttempt(attempt: Attempt): Attempt =
        client().from("attempts").insert(attempt) {
            select()
        }.decodeSingle()

    suspend fun saveAnswers(answers: List<Answer>) {
        client().from("answers").insert(answers)
    }

    suspend fun fetchUserAttempts(userId: String): List<Attempt> =
        client().from("attempts").select {
            filter {
                eq("user_id", userId)
            }
        }.decodeList()

    suspend fun updateUserProgress(userId: String, streak: Int, xp: Int) {
        client().from("users").update(
            buildJsonObject {
                put("streak", streak)
                put("xp", xp)
            }
        ) {
            filter { eq("id", userId) }
        }
    }
}
