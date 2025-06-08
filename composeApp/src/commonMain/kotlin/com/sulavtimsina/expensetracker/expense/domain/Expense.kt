package com.sulavtimsina.expensetracker.expense.domain

import kotlinx.datetime.LocalDateTime

data class Expense(
    val id: String,
    val amount: Double,
    val category: ExpenseCategory,
    val note: String?,
    val date: LocalDateTime,
    val imagePath: String? = null
)
