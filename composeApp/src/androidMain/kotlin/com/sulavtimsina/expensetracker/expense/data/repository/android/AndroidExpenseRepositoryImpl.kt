package com.sulavtimsina.expensetracker.expense.data.repository.android

import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.data.cloud.android.AndroidFirebaseSource
import com.sulavtimsina.expensetracker.expense.data.cloud.mappers.toDomainExpense
import com.sulavtimsina.expensetracker.expense.data.cloud.mappers.toFirebaseExpense
import com.sulavtimsina.expensetracker.expense.data.database.ExpenseDatabaseSource
import com.sulavtimsina.expensetracker.expense.data.mappers.toDomainExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseError
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class AndroidExpenseRepositoryImpl(
    private val databaseSource: ExpenseDatabaseSource,
    private val firebaseSource: AndroidFirebaseSource
) : ExpenseRepository {
    
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    init {
        // Initialize Firebase auth and start sync
        scope.launch {
            firebaseSource.signInAnonymously()
            startRealtimeSync()
        }
    }
    
    private fun startRealtimeSync() {
        scope.launch {
            firebaseSource.getAllExpenses()
                .catch { /* Handle errors silently */ }
                .collectLatest { firebaseExpenses ->
                    // Update local database with cloud changes
                    firebaseExpenses.forEach { firebaseExpense ->
                        try {
                            val domainExpense = firebaseExpense.toDomainExpense()
                            databaseSource.insertExpense(
                                domainExpense.id,
                                domainExpense.amount,
                                domainExpense.category.displayName,
                                domainExpense.note,
                                domainExpense.date.toString(),
                                domainExpense.imagePath
                            )
                        } catch (e: Exception) {
                            // Skip invalid entries
                        }
                    }
                }
        }
    }

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
            Result.Error(ExpenseError.DATABASE_ERROR)
        }
    }

    override suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            // Insert to local database first
            databaseSource.insertExpense(
                id = expense.id,
                amount = expense.amount,
                category = expense.category.displayName,
                note = expense.note,
                date = expense.date.toString(),
                imagePath = expense.imagePath
            )
            
            // Sync to Firebase in background
            scope.launch {
                val userId = firebaseSource.getCurrentUserId() ?: return@launch
                val firebaseExpense = expense.toFirebaseExpense(userId)
                firebaseSource.insertExpense(firebaseExpense)
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DATABASE_ERROR)
        }
    }

    override suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            // Update local database first
            databaseSource.updateExpense(
                id = expense.id,
                amount = expense.amount,
                category = expense.category.displayName,
                note = expense.note,
                date = expense.date.toString(),
                imagePath = expense.imagePath
            )
            
            // Sync to Firebase in background
            scope.launch {
                val userId = firebaseSource.getCurrentUserId() ?: return@launch
                val firebaseExpense = expense.toFirebaseExpense(userId)
                firebaseSource.updateExpense(firebaseExpense)
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DATABASE_ERROR)
        }
    }

    override suspend fun deleteExpense(id: String): Result<Unit, ExpenseError> {
        return try {
            // Delete from local database
            databaseSource.deleteExpense(id)
            
            // Delete from Firebase in background
            scope.launch {
                firebaseSource.deleteExpense(id)
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DATABASE_ERROR)
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
        endDate: LocalDateTime
    ): Flow<List<Expense>> {
        return databaseSource.getExpensesByDateRange(
            startDate.toString(),
            endDate.toString()
        ).map { expenses ->
            expenses.map { it.toDomainExpense() }
        }.catch { 
            emit(emptyList()) 
        }
    }
}
