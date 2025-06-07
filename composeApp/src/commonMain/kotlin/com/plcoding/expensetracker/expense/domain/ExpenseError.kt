package com.plcoding.expensetracker.expense.domain

import com.plcoding.expensetracker.core.domain.Error

enum class ExpenseError : Error {
    UNKNOWN_ERROR,
    INVALID_AMOUNT,
    INVALID_DATE,
    EXPENSE_NOT_FOUND,
    DATABASE_ERROR,
    IMAGE_PROCESSING_ERROR
}
