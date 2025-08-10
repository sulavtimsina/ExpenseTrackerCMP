package com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense

import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime

sealed interface AddEditExpenseAction {
    data class OnAmountChange(val amount: String) : AddEditExpenseAction

    data class OnCategoryChange(val category: ExpenseCategory) : AddEditExpenseAction

    data class OnNoteChange(val note: String) : AddEditExpenseAction

    data class OnDateChange(val date: LocalDateTime) : AddEditExpenseAction

    data class OnImagePathChange(val imagePath: String?) : AddEditExpenseAction

    data class LoadExpense(val expenseId: String) : AddEditExpenseAction

    data object OnSave : AddEditExpenseAction

    data object OnClearError : AddEditExpenseAction
}
