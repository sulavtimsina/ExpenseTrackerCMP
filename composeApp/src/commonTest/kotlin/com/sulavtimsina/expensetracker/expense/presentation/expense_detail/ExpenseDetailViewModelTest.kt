package com.sulavtimsina.expensetracker.expense.presentation.expense_detail

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.presentation.expense_list.FakeExpenseRepository
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExpenseDetailViewModelTest {

    private lateinit var fakeRepository: FakeExpenseRepository
    private lateinit var viewModel: ExpenseDetailViewModel

    @BeforeTest
    fun setup() {
        fakeRepository = FakeExpenseRepository()
        viewModel = ExpenseDetailViewModel(fakeRepository)
    }

    @Test
    fun `load expense succeeds when expense exists`() = runTest {
        // Given
        val expense = Expense(
            id = "1",
            amount = 25.50,
            category = ExpenseCategory.FOOD,
            note = "Lunch",
            date = LocalDateTime(2024, 1, 15, 12, 0),
            imagePath = null
        )
        fakeRepository.setExpenses(listOf(expense))

        // When
        viewModel.onAction(ExpenseDetailAction.LoadExpense("1"))

        // Then
        assertEquals(expense, viewModel.state.value.expense)
        assertEquals(false, viewModel.state.value.isLoading)
        assertNull(viewModel.state.value.errorMessage)
    }

    @Test
    fun `load expense fails when expense does not exist`() = runTest {
        // Given
        fakeRepository.setExpenses(emptyList())

        // When
        viewModel.onAction(ExpenseDetailAction.LoadExpense("nonexistent"))

        // Then
        assertNull(viewModel.state.value.expense)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `delete expense succeeds`() = runTest {
        // Given
        val expense = Expense(
            id = "1",
            amount = 25.50,
            category = ExpenseCategory.FOOD,
            note = "Lunch",
            date = LocalDateTime(2024, 1, 15, 12, 0),
            imagePath = null
        )
        fakeRepository.setExpenses(listOf(expense))
        viewModel.onAction(ExpenseDetailAction.LoadExpense("1"))

        // When
        viewModel.onAction(ExpenseDetailAction.OnDeleteExpense)

        // Then
        assertTrue(viewModel.state.value.isDeleted)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `delete expense fails when repository returns error`() = runTest {
        // Given
        val expense = Expense(
            id = "1",
            amount = 25.50,
            category = ExpenseCategory.FOOD,
            note = "Lunch",
            date = LocalDateTime(2024, 1, 15, 12, 0),
            imagePath = null
        )
        fakeRepository.setExpenses(listOf(expense))
        fakeRepository.setShouldReturnError(true)
        viewModel.onAction(ExpenseDetailAction.LoadExpense("1"))

        // When
        viewModel.onAction(ExpenseDetailAction.OnDeleteExpense)

        // Then
        assertEquals(false, viewModel.state.value.isDeleted)
        assertNotNull(viewModel.state.value.errorMessage)
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun `clear error sets error message to null`() = runTest {
        // When
        viewModel.onAction(ExpenseDetailAction.OnClearError)

        // Then
        assertNull(viewModel.state.value.errorMessage)
    }
}
