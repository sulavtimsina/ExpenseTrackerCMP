package com.sulavtimsina.expensetracker.expense.data.cloud.mappers

import com.sulavtimsina.expensetracker.expense.data.cloud.model.SupabaseExpense
import com.sulavtimsina.expensetracker.expense.data.cloud.model.SupabaseExpenseInsert
import com.sulavtimsina.expensetracker.expense.data.cloud.model.SupabaseExpenseUpdate
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun SupabaseExpense.toDomainExpense(): Expense {
    return Expense(
        id = id,
        amount = amount,
        category = ExpenseCategory.fromDisplayName(category),
        note = note,
        date = date.toLocalDateTime(TimeZone.currentSystemDefault()),
        imagePath = imagePath,
    )
}

fun Expense.toSupabaseExpenseInsert(userId: String): SupabaseExpenseInsert {
    return SupabaseExpenseInsert(
        id = id,
        userId = userId,
        amount = amount,
        category = category.displayName,
        note = note,
        date = date.toInstant(TimeZone.currentSystemDefault()),
        imagePath = imagePath,
    )
}

fun Expense.toSupabaseExpenseUpdate(): SupabaseExpenseUpdate {
    return SupabaseExpenseUpdate(
        amount = amount,
        category = category.displayName,
        note = note,
        date = date.toInstant(TimeZone.currentSystemDefault()),
        imagePath = imagePath,
    )
}
