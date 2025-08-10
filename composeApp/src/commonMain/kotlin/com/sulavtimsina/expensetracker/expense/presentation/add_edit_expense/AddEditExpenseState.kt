package com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense

import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AddEditExpenseState(
    val expenseId: String? = null,
    val amount: String = "",
    val category: ExpenseCategory? = null,
    val note: String = "",
    val date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val imagePath: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val amountError: String? = null,
)
