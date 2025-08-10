package com.sulavtimsina.expensetracker.expense.data.repository

import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.data.database.ExpenseDatabaseSource
import com.sulavtimsina.expensetracker.expense.data.mappers.toDomainExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseError
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class ExpenseRepositoryImplLocal(
    private val databaseSource: ExpenseDatabaseSource,
) : ExpenseRepository {
    override fun getAllExpenses(): Flow<List<Expense>> {
        return databaseSource.getAllExpenses()
            .map { expenses ->
                expenses.map { it.toDomainExpense() }
            }
            .catch {
                emit(emptyList())
            }
    }

    override suspend fun getExpenseById(id: String): Result<Expense?, ExpenseError> {
        return try {
            val expense = databaseSource.getExpenseById(id)?.toDomainExpense()
            Result.Success(expense)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Database operation failed"))
        }
    }

    override suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            databaseSource.insertExpense(
                id = expense.id,
                amount = expense.amount,
                category = expense.category.displayName,
                note = expense.note,
                date = expense.date.toString(),
                imagePath = expense.imagePath,
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Database operation failed"))
        }
    }

    override suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            databaseSource.updateExpense(
                id = expense.id,
                amount = expense.amount,
                category = expense.category.displayName,
                note = expense.note,
                date = expense.date.toString(),
                imagePath = expense.imagePath,
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Database operation failed"))
        }
    }

    override suspend fun deleteExpense(id: String): Result<Unit, ExpenseError> {
        return try {
            databaseSource.deleteExpense(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Database operation failed"))
        }
    }

    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return databaseSource.getExpensesByCategory(category.displayName)
            .map { expenses ->
                expenses.map { it.toDomainExpense() }
            }
            .catch {
                emit(emptyList())
            }
    }

    override fun getExpensesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Flow<List<Expense>> {
        return databaseSource.getExpensesByDateRange(
            startDate.toString(),
            endDate.toString(),
        ).map { expenses ->
            expenses.map { it.toDomainExpense() }
        }.catch {
            emit(emptyList())
        }
    }
}
