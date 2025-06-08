package com.sulavtimsina.expensetracker.expense.presentation.expense_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sulavtimsina.expensetracker.core.domain.onError
import com.sulavtimsina.expensetracker.core.domain.onSuccess
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: ExpenseDetailAction) {
        when (action) {
            is ExpenseDetailAction.LoadExpense -> {
                loadExpense(action.expenseId)
            }
            ExpenseDetailAction.OnDeleteExpense -> {
                deleteExpense()
            }
            ExpenseDetailAction.OnClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }
        }
    }

    private fun loadExpense(expenseId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            expenseRepository.getExpenseById(expenseId)
                .onSuccess { expense ->
                    _state.value = _state.value.copy(
                        expense = expense,
                        isLoading = false
                    )
                }
                .onError { error ->
                    _state.value = _state.value.copy(
                        errorMessage = "Failed to load expense: ${error.name}",
                        isLoading = false
                    )
                }
        }
    }

    private fun deleteExpense() {
        val expense = _state.value.expense ?: return
        
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            expenseRepository.deleteExpense(expense.id)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isDeleted = true
                    )
                }
                .onError { error ->
                    _state.value = _state.value.copy(
                        errorMessage = "Failed to delete expense: ${error.name}",
                        isLoading = false
                    )
                }
        }
    }
}
