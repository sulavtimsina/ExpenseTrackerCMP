package com.sulavtimsina.expensetracker.expense.presentation.expense_detail

sealed interface ExpenseDetailAction {
    data class LoadExpense(val expenseId: String) : ExpenseDetailAction
    data object OnDeleteExpense : ExpenseDetailAction
    data object OnClearError : ExpenseDetailAction
}
