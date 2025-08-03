package com.sulavtimsina.expensetracker.expense.data.repository

import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.data.cloud.ExpenseCloudSource
import com.sulavtimsina.expensetracker.expense.data.mappers.toDatabaseExpense
import com.sulavtimsina.expensetracker.expense.data.database.ExpenseDatabaseSource
import com.sulavtimsina.expensetracker.expense.data.mappers.toDomainExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseError
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ExpenseRepositoryImplHybrid(
    private val localSource: ExpenseDatabaseSource,
    private val cloudSource: ExpenseCloudSource
) : ExpenseRepository {
    
    private val syncScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    
    init {
        // Start background sync when repository is created
        startBackgroundSync()
    }
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        // Always return local data for immediate UI updates
        return localSource.getAllExpenses().map { databaseExpenses ->
            databaseExpenses.map { it.toDomainExpense() }
        }
    }
    
    override fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> {
        return localSource.getExpensesByCategory(category.displayName).map { databaseExpenses ->
            databaseExpenses.map { it.toDomainExpense() }
        }
    }
    
    override fun getExpensesByDateRange(
        startDate: kotlinx.datetime.LocalDateTime,
        endDate: kotlinx.datetime.LocalDateTime
    ): Flow<List<Expense>> {
        return localSource.getExpensesByDateRange(
            startDate.toString(),
            endDate.toString()
        ).map { databaseExpenses ->
            databaseExpenses.map { it.toDomainExpense() }
        }
    }
    
    override suspend fun getExpenseById(id: String): Result<Expense?, ExpenseError> {
        return try {
            val databaseExpense = localSource.getExpenseById(id)
            Result.Success(databaseExpense?.toDomainExpense())
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Failed to get expense: ${e.message}"))
        }
    }
    
    override suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            // Insert locally first for immediate feedback
            val dbExpense = expense.toDatabaseExpense()
            localSource.insertExpense(
                dbExpense.id,
                dbExpense.amount,
                dbExpense.category,
                dbExpense.note,
                dbExpense.date,
                dbExpense.imagePath
            )
            
            // Sync to cloud in background
            syncScope.launch {
                syncExpenseToCloud(expense)
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Failed to insert expense: ${e.message}"))
        }
    }
    
    override suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            // Update locally first
            val dbExpense = expense.toDatabaseExpense()
            localSource.updateExpense(
                dbExpense.id,
                dbExpense.amount,
                dbExpense.category,
                dbExpense.note,
                dbExpense.date,
                dbExpense.imagePath
            )
            
            // Sync to cloud in background
            syncScope.launch {
                syncExpenseToCloud(expense)
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Failed to update expense: ${e.message}"))
        }
    }
    
    override suspend fun deleteExpense(id: String): Result<Unit, ExpenseError> {
        return try {
            // Delete locally first
            localSource.deleteExpense(id)
            
            // Delete from cloud in background
            syncScope.launch {
                cloudSource.deleteExpense(id)
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.DatabaseError("Failed to delete expense: ${e.message}"))
        }
    }
    
    suspend fun signInAndStartSync(): Result<String, ExpenseError> {
        val signInResult = cloudSource.signInAnonymously()
        
        if (signInResult is Result.Success) {
            // Start initial sync after successful sign-in
            syncScope.launch {
                performInitialSync()
            }
        }
        
        return signInResult
    }
    
    private fun startBackgroundSync() {
        syncScope.launch {
            // Only start sync if user is already signed in
            if (cloudSource.getCurrentUserId() != null) {
                performInitialSync()
                
                // Set up real-time sync
                cloudSource.getAllExpenses().collect { cloudExpenses ->
                    syncFromCloudToLocal(cloudExpenses)
                }
            }
        }
    }
    
    private suspend fun performInitialSync() {
        try {
            // For now, let's focus on syncing from cloud to local
            cloudSource.getAllExpenses().collect { cloudExpenses ->
                syncFromCloudToLocal(cloudExpenses)
            }
        } catch (e: Exception) {
            // Log error but don't crash the app
            println("Initial sync failed: ${e.message}")
        }
    }
    
    private suspend fun syncExpenseToCloud(expense: Expense) {
        val userId = cloudSource.getCurrentUserId()
        if (userId == null) {
            println("Cannot sync to cloud: User not authenticated")
            return
        }
        
        try {
            println("Syncing expense ${expense.id} to cloud")
            val result = cloudSource.syncExpense(expense)
            when (result) {
                is Result.Success -> println("Successfully synced expense ${expense.id}")
                is Result.Error -> println("Failed to sync expense ${expense.id}: ${result.error.name}")
            }
        } catch (e: Exception) {
            // Log error but don't crash the app
            println("Failed to sync expense to cloud: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private suspend fun syncFromCloudToLocal(cloudExpenses: List<Expense>) {
        try {
            cloudExpenses.forEach { cloudExpense ->
                // Check if expense exists locally
                val existingExpense = getExpenseById(cloudExpense.id)
                val dbExpense = cloudExpense.toDatabaseExpense()
                
                when (existingExpense) {
                    is Result.Success -> {
                        if (existingExpense.data == null) {
                            // Expense doesn't exist locally, insert it
                            localSource.insertExpense(
                                dbExpense.id,
                                dbExpense.amount,
                                dbExpense.category,
                                dbExpense.note,
                                dbExpense.date,
                                dbExpense.imagePath
                            )
                        } else {
                            // Expense exists, update it (cloud version is considered authoritative for conflicts)
                            localSource.updateExpense(
                                dbExpense.id,
                                dbExpense.amount,
                                dbExpense.category,
                                dbExpense.note,
                                dbExpense.date,
                                dbExpense.imagePath
                            )
                        }
                    }
                    is Result.Error -> {
                        // Error getting expense, try to insert anyway
                        localSource.insertExpense(
                            dbExpense.id,
                            dbExpense.amount,
                            dbExpense.category,
                            dbExpense.note,
                            dbExpense.date,
                            dbExpense.imagePath
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Log error but don't crash the app
            println("Failed to sync from cloud to local: ${e.message}")
        }
    }
}
