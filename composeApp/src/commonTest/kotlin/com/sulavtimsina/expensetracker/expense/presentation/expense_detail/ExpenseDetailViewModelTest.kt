package com.sulavtimsina.expensetracker.expense.presentation.expense_detail

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.test.FakeExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.datetime.LocalDateTime
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeExpenseRepository
    private lateinit var viewModel: ExpenseDetailViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeExpenseRepository()
        viewModel = ExpenseDetailViewModel(fakeRepository)
    }
    
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
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
        advanceUntilIdle()

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
        advanceUntilIdle()

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
        advanceUntilIdle()

        // When
        viewModel.onAction(ExpenseDetailAction.OnDeleteExpense)
        advanceUntilIdle()

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
        advanceUntilIdle()

        // When
        viewModel.onAction(ExpenseDetailAction.OnDeleteExpense)
        advanceUntilIdle()

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
