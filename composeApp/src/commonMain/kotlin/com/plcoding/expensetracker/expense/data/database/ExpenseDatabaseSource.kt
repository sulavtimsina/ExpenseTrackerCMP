package com.plcoding.expensetracker.expense.data.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.plcoding.expensetracker.database.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ExpenseDatabaseSource(
    private val database: ExpenseDatabase
) {
    private val queries = database.expenseQueries

    fun getAllExpenses(): Flow<List<com.plcoding.expensetracker.database.Expense>> {
        return queries.selectAllExpenses()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun getExpenseById(id: String): com.plcoding.expensetracker.database.Expense? {
        return queries.selectExpenseById(id).executeAsOneOrNull()
    }

    suspend fun insertExpense(
        id: String,
        amount: Double,
        category: String,
        note: String?,
        date: String,
        imagePath: String?
    ) {
        queries.insertExpense(id, amount, category, note, date, imagePath)
    }

    suspend fun updateExpense(
        id: String,
        amount: Double,
        category: String,
        note: String?,
        date: String,
        imagePath: String?
    ) {
        queries.updateExpense(amount, category, note, date, imagePath, id)
    }

    suspend fun deleteExpense(id: String) {
        queries.deleteExpense(id)
    }

    fun getExpensesByCategory(category: String): Flow<List<com.plcoding.expensetracker.database.Expense>> {
        return queries.selectExpensesByCategory(category)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun getExpensesByDateRange(
        startDate: String,
        endDate: String
    ): Flow<List<com.plcoding.expensetracker.database.Expense>> {
        return queries.selectExpensesByDateRange(startDate, endDate)
            .asFlow()
            .mapToList(Dispatchers.Default)
    }
}
