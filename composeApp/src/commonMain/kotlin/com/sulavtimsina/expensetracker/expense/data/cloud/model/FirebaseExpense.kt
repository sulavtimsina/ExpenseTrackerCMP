package com.sulavtimsina.expensetracker.expense.data.cloud.model

data class FirebaseExpense(
    val id: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val note: String? = null,
    val date: String = "",
    val imagePath: String? = null,
    val userId: String = "",
    val lastModified: Long = 0L,
    val isDeleted: Boolean = false
) {
    // No-argument constructor required for Firestore
    constructor() : this("", 0.0, "", null, "", null, "", 0L, false)
}
