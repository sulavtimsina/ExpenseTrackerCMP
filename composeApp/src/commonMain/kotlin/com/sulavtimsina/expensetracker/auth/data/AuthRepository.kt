package com.sulavtimsina.expensetracker.auth.data

import com.sulavtimsina.expensetracker.auth.domain.AuthError
import com.sulavtimsina.expensetracker.core.data.SupabaseClient
import com.sulavtimsina.expensetracker.core.domain.Result
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository {
    private val auth = SupabaseClient.client.auth

    val isAuthenticated: Flow<Boolean> =
        auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> true
                else -> false
            }
        }

    val currentUser: Flow<String?> =
        auth.sessionStatus.map { status ->
            when (status) {
                is SessionStatus.Authenticated -> status.session.user?.id
                else -> null
            }
        }

    suspend fun signUp(
        email: String,
        password: String,
    ): Result<String, AuthError> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val userId = auth.currentUserOrNull()?.id ?: return Result.Error(AuthError.UnknownError("Failed to get user ID"))
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(AuthError.UnknownError(e.message ?: "Sign up failed"))
        }
    }

    suspend fun signIn(
        email: String,
        password: String,
    ): Result<String, AuthError> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val userId = auth.currentUserOrNull()?.id ?: return Result.Error(AuthError.UnknownError("Failed to get user ID"))
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(AuthError.UnknownError(e.message ?: "Sign in failed"))
        }
    }

    suspend fun signInAnonymously(): Result<String, AuthError> {
        return try {
            auth.signInAnonymously()
            val userId = auth.currentUserOrNull()?.id ?: return Result.Error(AuthError.UnknownError("Failed to get user ID"))
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(AuthError.UnknownError(e.message ?: "Anonymous sign in failed"))
        }
    }

    suspend fun signOut(): Result<Unit, AuthError> {
        return try {
            auth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AuthError.UnknownError(e.message ?: "Sign out failed"))
        }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUserOrNull()?.id
    }
}
