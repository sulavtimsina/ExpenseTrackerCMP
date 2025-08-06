package com.sulavtimsina.expensetracker.auth.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthErrorTest {
    
    @Test
    fun `NetworkError should have correct name`() {
        val error = AuthError.NetworkError
        assertEquals("Network error occurred", error.name)
    }
    
    @Test
    fun `UnauthorizedError should have correct name`() {
        val error = AuthError.UnauthorizedError
        assertEquals("Unauthorized access", error.name)
    }
    
    @Test
    fun `UnknownError should have custom message`() {
        val customMessage = "Custom error message"
        val error = AuthError.UnknownError(customMessage)
        assertEquals(customMessage, error.name)
    }
}