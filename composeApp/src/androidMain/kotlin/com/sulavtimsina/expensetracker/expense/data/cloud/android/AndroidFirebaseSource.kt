package com.sulavtimsina.expensetracker.expense.data.cloud.android

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sulavtimsina.expensetracker.expense.data.cloud.model.FirebaseExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AndroidFirebaseSource {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    
    companion object {
        private const val COLLECTION_EXPENSES = "expenses"
    }
    
    suspend fun signInAnonymously(): Result<String> {
        return try {
            val result = auth.signInAnonymously().await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    
    fun getAllExpenses(): Flow<List<FirebaseExpense>> = callbackFlow {
        val userId = getCurrentUserId()
        if (userId == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        
        val listener = firestore.collection(COLLECTION_EXPENSES)
            .whereEqualTo("userId", userId)
            .whereEqualTo("isDeleted", false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val expenses = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(FirebaseExpense::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                
                trySend(expenses)
            }
        
        awaitClose { listener.remove() }
    }
    
    suspend fun insertExpense(firebaseExpense: FirebaseExpense): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_EXPENSES)
                .document(firebaseExpense.id)
                .set(firebaseExpense)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateExpense(firebaseExpense: FirebaseExpense): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_EXPENSES)
                .document(firebaseExpense.id)
                .set(firebaseExpense)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteExpense(id: String): Result<Unit> {
        return try {
            firestore.collection(COLLECTION_EXPENSES)
                .document(id)
                .update(
                    "isDeleted", true,
                    "lastModified", System.currentTimeMillis()
                )
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
