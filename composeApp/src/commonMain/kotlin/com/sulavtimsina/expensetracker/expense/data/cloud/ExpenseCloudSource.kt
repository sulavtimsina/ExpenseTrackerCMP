package com.sulavtimsina.expensetracker.expense.data.cloud

import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseError
import kotlinx.coroutines.flow.Flow

interface ExpenseCloudSource {
    suspend fun signInAnonymously(): Result<String, ExpenseError>
    fun getCurrentUserId(): String?
    fun getAllExpenses(): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError>
    suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError>
    suspend fun deleteExpense(id: String): Result<Unit, ExpenseError>
    suspend fun syncExpense(expense: Expense): Result<Unit, ExpenseError>
}
