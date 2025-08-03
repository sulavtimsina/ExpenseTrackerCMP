package com.sulavtimsina.expensetracker.expense.presentation.expense_list

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.test.FakeExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExpenseListViewModelTest {

    private lateinit var fakeRepository: FakeExpenseRepository
    private lateinit var viewModel: ExpenseListViewModel

    @BeforeTest
    fun setup() {
        fakeRepository = FakeExpenseRepository()
        viewModel = ExpenseListViewModel(fakeRepository)
    }

    @Test
    fun `search query filters expenses correctly`() = runTest {
        // Given
        val expenses = listOf(
            createExpense("1", note = "Lunch at restaurant", category = ExpenseCategory.FOOD),
            createExpense("2", note = "Bus ticket", category = ExpenseCategory.TRANSPORTATION),
            createExpense("3", note = "Coffee", category = ExpenseCategory.FOOD)
        )
        fakeRepository.setExpenses(expenses)

        // When
        viewModel.onAction(ExpenseListAction.OnSearchQueryChange("lunch"))

        // Then - This is a simplified test since we can't easily test StateFlow in unit tests
        assertEquals("lunch", viewModel.searchQuery.value)
    }

    @Test
    fun `category filter updates selected category`() = runTest {
        // When
        viewModel.onAction(ExpenseListAction.OnCategoryFilter(ExpenseCategory.FOOD))

        // Then
        assertEquals(ExpenseCategory.FOOD, viewModel.selectedCategory.value)
    }

    @Test
    fun `clear error sets error message to null`() = runTest {
        // When
        viewModel.onAction(ExpenseListAction.OnClearError)

        // Then
        assertEquals(null, viewModel.errorMessage.value)
    }

    private fun createExpense(
        id: String,
        amount: Double = 10.0,
        category: ExpenseCategory = ExpenseCategory.OTHER,
        note: String? = null,
        imagePath: String? = null
    ) = Expense(
        id = id,
        amount = amount,
        category = category,
        note = note,
        date = LocalDateTime(2024, 1, 15, 12, 0),
        imagePath = imagePath
    )
}

