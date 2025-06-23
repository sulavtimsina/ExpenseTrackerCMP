package com.sulavtimsina.expensetracker.expense.data.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class SupabaseExpense(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val amount: Double,
    val category: String,
    val note: String? = null,
    val date: Instant,
    @SerialName("image_path")
    val imagePath: String? = null,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)

@Serializable
data class SupabaseExpenseInsert(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val amount: Double,
    val category: String,
    val note: String? = null,
    val date: Instant,
    @SerialName("image_path")
    val imagePath: String? = null
)

@Serializable
data class SupabaseExpenseUpdate(
    val amount: Double,
    val category: String,
    val note: String? = null,
    val date: Instant,
    @SerialName("image_path")
    val imagePath: String? = null
)
