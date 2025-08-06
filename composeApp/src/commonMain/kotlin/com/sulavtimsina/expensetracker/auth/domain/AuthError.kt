package com.sulavtimsina.expensetracker.auth.domain

import com.sulavtimsina.expensetracker.core.domain.Error

sealed class AuthError : Error {
    data object NetworkError : AuthError()
    data object UnauthorizedError : AuthError()
    data class UnknownError(val message: String) : AuthError()
    
    override val name: String
        get() = when (this) {
            is NetworkError -> "Network error occurred"
            is UnauthorizedError -> "Unauthorized access"
            is UnknownError -> message
        }
}