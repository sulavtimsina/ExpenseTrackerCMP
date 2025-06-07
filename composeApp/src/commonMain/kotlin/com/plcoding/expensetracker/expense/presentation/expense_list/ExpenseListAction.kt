package com.plcoding.expensetracker.expense.presentation.expense_list

import com.plcoding.expensetracker.expense.domain.Expense
import com.plcoding.expensetracker.expense.domain.ExpenseCategory

sealed interface ExpenseListAction {
    data class OnSearchQueryChange(val query: String) : ExpenseListAction
    data class OnCategoryFilter(val category: ExpenseCategory?) : ExpenseListAction
    data class OnDeleteExpense(val expense: Expense) : ExpenseListAction
    data object OnClearError : ExpenseListAction
}
