package com.sulavtimsina.expensetracker.expense.data.cloud

import com.sulavtimsina.expensetracker.core.data.SupabaseClient
import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.data.cloud.mappers.toDomainExpense
import com.sulavtimsina.expensetracker.expense.data.cloud.mappers.toSupabaseExpenseInsert
import com.sulavtimsina.expensetracker.expense.data.cloud.mappers.toSupabaseExpenseUpdate
import com.sulavtimsina.expensetracker.expense.data.cloud.model.SupabaseExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseError
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf

class SupabaseExpenseSource : ExpenseCloudSource {
    
    private val supabase = SupabaseClient.client
    
    override suspend fun signInAnonymously(): Result<String, ExpenseError> {
        return try {
            supabase.auth.signInAnonymously()
            // After sign in, get the current user ID
            val userId = supabase.auth.currentUserOrNull()?.id
            if (userId != null) {
                Result.Success(userId)
            } else {
                Result.Error(ExpenseError.CloudSync("Failed to get user ID after sign in"))
            }
        } catch (e: Exception) {
            Result.Error(ExpenseError.CloudSync("Failed to sign in: ${e.message}"))
        }
    }
    
    override fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        val userId = getCurrentUserId()
        return if (userId != null) {
            try {
                // For now, return empty flow until we implement proper real-time sync
                // This will be a simple polling mechanism
                flowOf(emptyList())
            } catch (e: Exception) {
                flowOf(emptyList())
            }
        } else {
            flowOf(emptyList())
        }
    }
    
    override suspend fun insertExpense(expense: Expense): Result<Unit, ExpenseError> {
        val userId = getCurrentUserId() ?: return Result.Error(
            ExpenseError.CloudSync("User not authenticated")
        )
        
        return try {
            println("Attempting to insert expense to Supabase: ${expense.id}")
            println("User ID: $userId")
            val supabaseExpense = expense.toSupabaseExpenseInsert(userId)
            println("Supabase expense data: $supabaseExpense")
            
            supabase.from("expenses").insert(supabaseExpense)
            println("Successfully inserted expense to Supabase")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("Failed to insert expense to Supabase: ${e.message}")
            e.printStackTrace()
            Result.Error(ExpenseError.CloudSync("Failed to insert expense: ${e.message}"))
        }
    }
    
    override suspend fun updateExpense(expense: Expense): Result<Unit, ExpenseError> {
        return try {
            supabase.from("expenses")
                .update(expense.toSupabaseExpenseUpdate()) {
                    filter {
                        eq("id", expense.id)
                    }
                }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.CloudSync("Failed to update expense: ${e.message}"))
        }
    }
    
    override suspend fun deleteExpense(id: String): Result<Unit, ExpenseError> {
        return try {
            supabase.from("expenses").delete {
                filter {
                    eq("id", id)
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(ExpenseError.CloudSync("Failed to delete expense: ${e.message}"))
        }
    }
    
    override suspend fun syncExpense(expense: Expense): Result<Unit, ExpenseError> {
        val userId = getCurrentUserId() ?: return Result.Error(
            ExpenseError.CloudSync("User not authenticated")
        )
        
        return try {
            println("Attempting to sync expense to Supabase: ${expense.id}")
            supabase.from("expenses").upsert(expense.toSupabaseExpenseInsert(userId))
            println("Successfully synced expense to Supabase")
            Result.Success(Unit)
        } catch (e: Exception) {
            println("Failed to sync expense to Supabase: ${e.message}")
            e.printStackTrace()
            Result.Error(ExpenseError.CloudSync("Failed to sync expense: ${e.message}"))
        }
    }
    
    // Helper method to fetch all expenses (manual sync)
    suspend fun fetchAllExpenses(): Result<List<Expense>, ExpenseError> {
        return try {
            val result = supabase.from("expenses")
                .select(Columns.ALL)
                .decodeList<SupabaseExpense>()
            
            val expenses = result.map { it.toDomainExpense() }
            Result.Success(expenses)
        } catch (e: Exception) {
            Result.Error(ExpenseError.CloudSync("Failed to fetch expenses: ${e.message}"))
        }
    }
}
