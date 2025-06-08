package com.sulavtimsina.expensetracker.expense.data.cloud.mappers

import com.sulavtimsina.expensetracker.expense.data.cloud.model.FirebaseExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock

fun Expense.toFirebaseExpense(userId: String): FirebaseExpense {
    return FirebaseExpense(
        id = id,
        amount = amount,
        category = category.displayName,
        note = note,
        date = date.toString(),
        imagePath = imagePath,
        userId = userId,
        lastModified = Clock.System.now().toEpochMilliseconds(),
        isDeleted = false
    )
}

fun FirebaseExpense.toDomainExpense(): Expense {
    return Expense(
        id = id,
        amount = amount,
        category = ExpenseCategory.fromDisplayName(category),
        note = note,
        date = LocalDateTime.parse(date),
        imagePath = imagePath
    )
}

// Extension function for ExpenseCategory to handle Firebase conversion
fun ExpenseCategory.Companion.fromDisplayName(displayName: String): ExpenseCategory {
    return ExpenseCategory.entries.find { it.displayName == displayName } 
        ?: ExpenseCategory.OTHER
}
