package com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sulavtimsina.expensetracker.core.domain.onError
import com.sulavtimsina.expensetracker.core.domain.onSuccess
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddEditExpenseViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditExpenseState())
    val state = _state.asStateFlow()

    fun onAction(action: AddEditExpenseAction) {
        when (action) {
            is AddEditExpenseAction.OnAmountChange -> {
                _state.value = _state.value.copy(
                    amount = action.amount,
                    amountError = null
                )
            }
            is AddEditExpenseAction.OnCategoryChange -> {
                _state.value = _state.value.copy(category = action.category)
            }
            is AddEditExpenseAction.OnNoteChange -> {
                _state.value = _state.value.copy(note = action.note)
            }
            is AddEditExpenseAction.OnDateChange -> {
                _state.value = _state.value.copy(date = action.date)
            }
            is AddEditExpenseAction.OnImagePathChange -> {
                _state.value = _state.value.copy(imagePath = action.imagePath)
            }
            AddEditExpenseAction.OnSave -> {
                saveExpense()
            }
            AddEditExpenseAction.OnClearError -> {
                _state.value = _state.value.copy(errorMessage = null)
            }
            is AddEditExpenseAction.LoadExpense -> {
                loadExpense(action.expenseId)
            }
        }
    }

    private fun loadExpense(expenseId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            expenseRepository.getExpenseById(expenseId)
                .onSuccess { expense ->
                    expense?.let {
                        _state.value = _state.value.copy(
                            expenseId = it.id,
                            amount = it.amount.toString(),
                            category = it.category,
                            note = it.note ?: "",
                            date = it.date,
                            imagePath = it.imagePath,
                            isLoading = false
                        )
                    }
                }
                .onError { error ->
                    _state.value = _state.value.copy(
                        errorMessage = "Failed to load expense: ${error.name}",
                        isLoading = false
                    )
                }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveExpense() {
        val currentState = _state.value
        
        val amount = currentState.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _state.value = currentState.copy(amountError = "Please enter a valid amount")
            return
        }

        if (currentState.category == null) {
            _state.value = currentState.copy(errorMessage = "Please select a category")
            return
        }

        viewModelScope.launch {
            _state.value = currentState.copy(isLoading = true)
            
            val expense = Expense(
                id = currentState.expenseId ?: Uuid.random().toString(),
                amount = amount,
                category = currentState.category,
                note = currentState.note.takeIf { it.isNotBlank() },
                date = currentState.date,
                imagePath = currentState.imagePath
            )

            val result = if (currentState.expenseId != null) {
                expenseRepository.updateExpense(expense)
            } else {
                expenseRepository.insertExpense(expense)
            }

            result
                .onSuccess {
                    _state.value = currentState.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }
                .onError { error ->
                    _state.value = currentState.copy(
                        errorMessage = "Failed to save expense: ${error.name}",
                        isLoading = false
                    )
                }
        }
    }
}
