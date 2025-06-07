package com.plcoding.expensetracker.expense.presentation.expense_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.expensetracker.core.domain.onError
import com.plcoding.expensetracker.core.domain.onSuccess
import com.plcoding.expensetracker.expense.domain.Expense
import com.plcoding.expensetracker.expense.domain.ExpenseCategory
import com.plcoding.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExpenseListViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ExpenseCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val expenses = combine(
        expenseRepository.getAllExpenses(),
        _searchQuery,
        _selectedCategory
    ) { expenses, query, category ->
        var filteredExpenses = expenses

        if (category != null) {
            filteredExpenses = filteredExpenses.filter { it.category == category }
        }

        if (query.isNotBlank()) {
            filteredExpenses = filteredExpenses.filter {
                it.note?.contains(query, ignoreCase = true) == true ||
                        it.category.displayName.contains(query, ignoreCase = true)
            }
        }

        filteredExpenses
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onAction(action: ExpenseListAction) {
        when (action) {
            is ExpenseListAction.OnSearchQueryChange -> {
                _searchQuery.value = action.query
            }
            is ExpenseListAction.OnCategoryFilter -> {
                _selectedCategory.value = action.category
            }
            is ExpenseListAction.OnDeleteExpense -> {
                deleteExpense(action.expense)
            }
            ExpenseListAction.OnClearError -> {
                _errorMessage.value = null
            }
        }
    }

    private fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            _isLoading.value = true
            expenseRepository.deleteExpense(expense.id)
                .onSuccess {
                    // Expense deleted successfully
                }
                .onError { error ->
                    _errorMessage.value = "Failed to delete expense: ${error.name}"
                }
            _isLoading.value = false
        }
    }
}
