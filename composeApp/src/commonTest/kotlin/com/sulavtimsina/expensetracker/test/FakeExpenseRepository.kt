package com.sulavtimsina.expensetracker.test

import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseError
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime

class FakeExpenseRepository : ExpenseRepository {
    private var expenses = listOf<Expense>()
    private var shouldReturnError = false

    fun setExpenses(expenses: List<Expense>) {
        this.expenses = expenses
    }

    fun setShouldReturnError(shouldReturnError: Boolean) {
        this.shouldReturnError = shouldReturnError
    }

    override fun getAllExpenses(): Flow<List<Expense>> {
        return flowOf(expenses)
    }

    override suspend fun getExpenseById(id: String): Result<Expense?, ExpenseError> {
        return if (shouldReturnError) {
            Result.Error(ExpenseError.ExpenseNotFound())
        } else {
            Result.Success(expenses.find { it.id == id })
        }
    }

    override suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError> {
        return if (shouldReturnError) {
            Result.Error(ExpenseError.DatabaseError())
        } else {
            expenses = expenses + expense
            Result.Success(Unit)
        }
    }

    override suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError> {
        return if (shouldReturnError) {
            Result.Error(ExpenseError.DatabaseError())
        } else {
            expenses = expenses.map { if (it.id == expense.id) expense else it }
            Result.Success(Unit)
        }
    }

    override suspend fun deleteExpense(id: String): Result<Unit, ExpenseError> {
        return if (shouldReturnError) {
            Result.Error(ExpenseError.DatabaseError())
        } else {
            expenses = expenses.filter { it.id != id }
            Result.Success(Unit)
        }
    }

    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return flowOf(expenses.filter { it.category == category })
    }

    override fun getExpensesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Expense>> {
        return flowOf(expenses.filter { it.date >= startDate && it.date <= endDate })
    }
}