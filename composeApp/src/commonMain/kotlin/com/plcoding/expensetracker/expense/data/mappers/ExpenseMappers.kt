package com.plcoding.expensetracker.expense.data.mappers

import com.plcoding.expensetracker.expense.domain.Expense
import com.plcoding.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime
import com.plcoding.expensetracker.database.Expense as DatabaseExpense

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
