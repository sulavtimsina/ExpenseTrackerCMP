package com.sulavtimsina.expensetracker.expense.domain

import com.sulavtimsina.expensetracker.core.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun getExpenseById(id: String): Result<Expense?, ExpenseError>
    suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError>
    suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError>
    suspend fun deleteExpense(id: String): Result<Unit, ExpenseError>
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>>
    fun getExpensesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Expense>>
}
