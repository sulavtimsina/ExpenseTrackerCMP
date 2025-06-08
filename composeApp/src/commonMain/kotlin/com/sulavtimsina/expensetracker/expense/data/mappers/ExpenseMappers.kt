package com.sulavtimsina.expensetracker.expense.data.mappers

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime
import com.sulavtimsina.expensetracker.database.Expense as DatabaseExpense

fun DatabaseExpense.toDomainExpense(): Expense {
    return Expense(
        id = id,
        amount = amount,
        category = ExpenseCategory.fromDisplayName(category),
        note = note,
        date = LocalDateTime.parse(date),
        imagePath = imagePath
    )
}

fun Expense.toDatabaseExpense(): DatabaseExpense {
    return DatabaseExpense(
        id = id,
        amount = amount,
        category = category.displayName,
        note = note,
        date = date.toString(),
        imagePath = imagePath
    )
}
