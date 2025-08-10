package com.sulavtimsina.expensetracker.expense.domain

import com.sulavtimsina.expensetracker.core.domain.Error
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExpenseErrorTest {
    @Test
    fun `ExpenseError implements Error interface`() {
        val error: Error = ExpenseError.UnknownError()
        assertTrue(error is ExpenseError)
    }

    @Test
    fun `UnknownError name property formats correctly with default message`() {
        val error = ExpenseError.UnknownError()
        assertEquals("Unknown error: An unknown error occurred", error.name)
    }

    @Test
    fun `UnknownError name property formats correctly with custom message`() {
        val customMessage = "Custom unknown error message"
        val error = ExpenseError.UnknownError(customMessage)
        assertEquals("Unknown error: $customMessage", error.name)
    }

    @Test
    fun `InvalidAmount name property formats correctly with default message`() {
        val error = ExpenseError.InvalidAmount()
        assertEquals("Invalid amount: Invalid expense amount", error.name)
    }

    @Test
    fun `InvalidAmount name property formats correctly with custom message`() {
        val customMessage = "Amount must be positive"
        val error = ExpenseError.InvalidAmount(customMessage)
        assertEquals("Invalid amount: $customMessage", error.name)
    }

    @Test
    fun `InvalidDate name property formats correctly with default message`() {
        val error = ExpenseError.InvalidDate()
        assertEquals("Invalid date: Invalid expense date", error.name)
    }

    @Test
    fun `InvalidDate name property formats correctly with custom message`() {
        val customMessage = "Date cannot be in the future"
        val error = ExpenseError.InvalidDate(customMessage)
        assertEquals("Invalid date: $customMessage", error.name)
    }

    @Test
    fun `ExpenseNotFound name property formats correctly with default message`() {
        val error = ExpenseError.ExpenseNotFound()
        assertEquals("Expense not found: Expense not found", error.name)
    }

    @Test
    fun `ExpenseNotFound name property formats correctly with custom message`() {
        val customMessage = "Expense with ID 123 not found"
        val error = ExpenseError.ExpenseNotFound(customMessage)
        assertEquals("Expense not found: $customMessage", error.name)
    }

    @Test
    fun `DatabaseError name property formats correctly with default message`() {
        val error = ExpenseError.DatabaseError()
        assertEquals("Database error: Database operation failed", error.name)
    }

    @Test
    fun `DatabaseError name property formats correctly with custom message`() {
        val customMessage = "Failed to connect to database"
        val error = ExpenseError.DatabaseError(customMessage)
        assertEquals("Database error: $customMessage", error.name)
    }

    @Test
    fun `ImageProcessingError name property formats correctly with default message`() {
        val error = ExpenseError.ImageProcessingError()
        assertEquals("Image processing error: Image processing failed", error.name)
    }

    @Test
    fun `ImageProcessingError name property formats correctly with custom message`() {
        val customMessage = "Failed to resize image"
        val error = ExpenseError.ImageProcessingError(customMessage)
        assertEquals("Image processing error: $customMessage", error.name)
    }

    @Test
    fun `CloudSync name property formats correctly with default message`() {
        val error = ExpenseError.CloudSync()
        assertEquals("Cloud sync error: Cloud sync failed", error.name)
    }

    @Test
    fun `CloudSync name property formats correctly with custom message`() {
        val customMessage = "Failed to sync with cloud storage"
        val error = ExpenseError.CloudSync(customMessage)
        assertEquals("Cloud sync error: $customMessage", error.name)
    }

    @Test
    fun `NetworkError name property formats correctly with default message`() {
        val error = ExpenseError.NetworkError()
        assertEquals("Network error: Network connection failed", error.name)
    }

    @Test
    fun `NetworkError name property formats correctly with custom message`() {
        val customMessage = "No internet connection available"
        val error = ExpenseError.NetworkError(customMessage)
        assertEquals("Network error: $customMessage", error.name)
    }

    @Test
    fun `all error types are data classes with equals and hashCode`() {
        val error1 = ExpenseError.UnknownError("test")
        val error2 = ExpenseError.UnknownError("test")
        val error3 = ExpenseError.UnknownError("different")

        assertEquals(error1, error2)
        assertEquals(error1.hashCode(), error2.hashCode())
        assertTrue(error1 != error3)
    }

    @Test
    fun `different error types are not equal even with same message`() {
        val unknownError = ExpenseError.UnknownError("test")
        val databaseError = ExpenseError.DatabaseError("test")

        assertTrue(unknownError != databaseError)
    }
}
