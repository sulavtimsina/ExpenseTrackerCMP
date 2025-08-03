package com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense

import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.test.FakeExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditExpenseViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeExpenseRepository
    private lateinit var viewModel: AddEditExpenseViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeExpenseRepository()
        viewModel = AddEditExpenseViewModel(fakeRepository)
    }
    
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `amount change updates state correctly`() = runTest {
        // When
        viewModel.onAction(AddEditExpenseAction.OnAmountChange("25.50"))

        // Then
        assertEquals("25.50", viewModel.state.value.amount)
        assertNull(viewModel.state.value.amountError)
    }

    @Test
    fun `category change updates state correctly`() = runTest {
        // When
        viewModel.onAction(AddEditExpenseAction.OnCategoryChange(ExpenseCategory.FOOD))

        // Then
        assertEquals(ExpenseCategory.FOOD, viewModel.state.value.category)
    }

    @Test
    fun `note change updates state correctly`() = runTest {
        // When
        viewModel.onAction(AddEditExpenseAction.OnNoteChange("Test note"))

        // Then
        assertEquals("Test note", viewModel.state.value.note)
    }

    @Test
    fun `save with invalid amount shows error`() = runTest {
        // Given
        viewModel.onAction(AddEditExpenseAction.OnAmountChange("invalid"))
        viewModel.onAction(AddEditExpenseAction.OnCategoryChange(ExpenseCategory.FOOD))

        // When
        viewModel.onAction(AddEditExpenseAction.OnSave)

        // Then
        assertNotNull(viewModel.state.value.amountError)
        assertEquals("Please enter a valid amount", viewModel.state.value.amountError)
    }

    @Test
    fun `save with negative amount shows error`() = runTest {
        // Given
        viewModel.onAction(AddEditExpenseAction.OnAmountChange("-10"))
        viewModel.onAction(AddEditExpenseAction.OnCategoryChange(ExpenseCategory.FOOD))

        // When
        viewModel.onAction(AddEditExpenseAction.OnSave)

        // Then
        assertNotNull(viewModel.state.value.amountError)
    }

    @Test
    fun `save without category shows error`() = runTest {
        // Given
        viewModel.onAction(AddEditExpenseAction.OnAmountChange("25.50"))

        // When
        viewModel.onAction(AddEditExpenseAction.OnSave)

        // Then
        assertNotNull(viewModel.state.value.errorMessage)
        assertEquals("Please select a category", viewModel.state.value.errorMessage)
    }

    @Test
    fun `save with valid data succeeds`() = runTest {
        // Given
        viewModel.onAction(AddEditExpenseAction.OnAmountChange("25.50"))
        viewModel.onAction(AddEditExpenseAction.OnCategoryChange(ExpenseCategory.FOOD))
        viewModel.onAction(AddEditExpenseAction.OnNoteChange("Lunch"))

        // When
        viewModel.onAction(AddEditExpenseAction.OnSave)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value.isSaved)
    }

    @Test
    fun `clear error sets error message to null`() = runTest {
        // When
        viewModel.onAction(AddEditExpenseAction.OnClearError)

        // Then
        assertNull(viewModel.state.value.errorMessage)
    }

    @Test
    fun `image path change updates state correctly`() = runTest {
        // When
        viewModel.onAction(AddEditExpenseAction.OnImagePathChange("/path/to/image.jpg"))

        // Then
        assertEquals("/path/to/image.jpg", viewModel.state.value.imagePath)
    }
}
