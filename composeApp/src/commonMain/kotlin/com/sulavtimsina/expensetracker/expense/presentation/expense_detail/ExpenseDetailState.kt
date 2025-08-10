package com.sulavtimsina.expensetracker.expense.presentation.expense_detail

import com.sulavtimsina.expensetracker.expense.domain.Expense

data class ExpenseDetailState(
    val expense: Expense? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val errorMessage: String? = null,
)
