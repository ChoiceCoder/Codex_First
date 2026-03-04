package com.govtprep.data.repository

import com.govtprep.data.model.UserProfile
import com.govtprep.data.remote.SupabaseDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val dataSource: SupabaseDataSource
) {
    suspend fun login(email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dataSource.login(email, password) }.map { }
    }

    suspend fun signUp(name: String, email: String, password: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            dataSource.signUp(email, password)
            val userId = dataSource.currentUserId() ?: error("User missing after signup")
            dataSource.upsertUserProfile(UserProfile(id = userId, email = email, name = name))
        }.map { }
    }

    suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { dataSource.logout() }.map { }
    }

    suspend fun currentProfile(): Result<UserProfile?> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = dataSource.currentUserId() ?: return@runCatching null
            dataSource.fetchUserProfile(userId)
        }
    }

    fun hasSession(): Boolean = dataSource.currentUserId() != null
}
