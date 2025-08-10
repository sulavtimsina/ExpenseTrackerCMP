package com.sulavtimsina.expensetracker.expense.domain

import com.sulavtimsina.expensetracker.core.domain.Error

sealed class ExpenseError : Error {
    override val name: String
        get() =
            when (this) {
                is UnknownError -> "Unknown error: $message"
                is InvalidAmount -> "Invalid amount: $message"
                is InvalidDate -> "Invalid date: $message"
                is ExpenseNotFound -> "Expense not found: $message"
                is DatabaseError -> "Database error: $message"
                is ImageProcessingError -> "Image processing error: $message"
                is CloudSync -> "Cloud sync error: $message"
                is NetworkError -> "Network error: $message"
            }

    data class UnknownError(val message: String = "An unknown error occurred") : ExpenseError()

    data class InvalidAmount(val message: String = "Invalid expense amount") : ExpenseError()

    data class InvalidDate(val message: String = "Invalid expense date") : ExpenseError()

    data class ExpenseNotFound(val message: String = "Expense not found") : ExpenseError()

    data class DatabaseError(val message: String = "Database operation failed") : ExpenseError()

    data class ImageProcessingError(val message: String = "Image processing failed") : ExpenseError()

    data class CloudSync(val message: String = "Cloud sync failed") : ExpenseError()

    data class NetworkError(val message: String = "Network connection failed") : ExpenseError()
}
