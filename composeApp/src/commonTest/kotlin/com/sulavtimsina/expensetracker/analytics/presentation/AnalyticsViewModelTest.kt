package com.sulavtimsina.expensetracker.analytics.presentation

import com.sulavtimsina.expensetracker.analytics.domain.AnalyticsData
import com.sulavtimsina.expensetracker.analytics.domain.AnalyticsPeriod
import com.sulavtimsina.expensetracker.analytics.domain.CategoryData
import com.sulavtimsina.expensetracker.analytics.domain.GetAnalyticsDataUseCase
import com.sulavtimsina.expensetracker.expense.domain.*
import com.sulavtimsina.expensetracker.test.FakeExpenseRepository
import com.sulavtimsina.expensetracker.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlinx.datetime.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeRepository: FakeExpenseRepository
    private lateinit var useCase: GetAnalyticsDataUseCase
    private lateinit var viewModel: AnalyticsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeExpenseRepository()
        useCase = GetAnalyticsDataUseCase(fakeRepository)
        viewModel = AnalyticsViewModel(useCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has default period and loading false`() {
        // Then
        assertEquals(AnalyticsPeriod.PeriodType.LAST_30_DAYS, viewModel.state.selectedPeriod.type)
        assertEquals(AnalyticsState.ChartType.CATEGORY_PIE, viewModel.state.selectedChartType)
        assertFalse(viewModel.state.isLoading)
        assertFalse(viewModel.state.showDatePicker)
        assertNull(viewModel.state.datePickerType)
    }

    @Test
    fun `onAction SelectPeriodType updates period and loads data`() = runTest {
        // Given
        val mockExpenses = listOf(
            createMockExpense("1", 100.0, ExpenseCategory.FOOD),
            createMockExpense("2", 200.0, ExpenseCategory.TRANSPORTATION)
        )
        fakeRepository.setExpenses(mockExpenses)

        // When
        viewModel.onAction(AnalyticsAction.SelectPeriodType(AnalyticsPeriod.PeriodType.LAST_3_MONTHS))
        advanceUntilIdle()

        // Then
        assertEquals(AnalyticsPeriod.PeriodType.LAST_3_MONTHS, viewModel.state.selectedPeriod.type)
        assertNotNull(viewModel.state.analyticsData)
        assertEquals(300.0, viewModel.state.analyticsData?.totalAmount)
    }

    @Test
    fun `onAction SelectChartType updates chart type`() {
        // When
        viewModel.onAction(AnalyticsAction.SelectChartType(AnalyticsState.ChartType.MONTHLY_TREND))

        // Then
        assertEquals(AnalyticsState.ChartType.MONTHLY_TREND, viewModel.state.selectedChartType)
    }

    @Test
    fun `onAction ShowDatePicker shows date picker`() {
        // When
        viewModel.onAction(AnalyticsAction.ShowDatePicker(AnalyticsState.DatePickerType.START_DATE))

        // Then
        assertTrue(viewModel.state.showDatePicker)
        assertEquals(AnalyticsState.DatePickerType.START_DATE, viewModel.state.datePickerType)
    }

    @Test
    fun `onAction SetCustomStartDate updates custom start date`() {
        // Given
        val newDate = LocalDateTime(2024, 1, 15, 0, 0)

        // When
        viewModel.onAction(AnalyticsAction.SetCustomStartDate(newDate))

        // Then
        assertEquals(newDate, viewModel.state.customStartDate)
    }

    @Test
    fun `onAction SetCustomEndDate updates custom end date`() {
        // Given
        val endDate = LocalDateTime(2024, 1, 31, 23, 59)

        // When
        viewModel.onAction(AnalyticsAction.SetCustomEndDate(endDate))

        // Then
        assertEquals(endDate, viewModel.state.customEndDate)
    }

    @Test
    fun `onAction HideDatePicker hides date picker`() {
        // Given
        viewModel.onAction(AnalyticsAction.ShowDatePicker(AnalyticsState.DatePickerType.START_DATE))
        assertTrue(viewModel.state.showDatePicker)

        // When
        viewModel.onAction(AnalyticsAction.HideDatePicker)

        // Then
        assertFalse(viewModel.state.showDatePicker)
        assertNull(viewModel.state.datePickerType)
    }

    @Test
    fun `onAction ApplyCustomDateRange applies custom date range`() = runTest {
        // Given
        val startDate = LocalDateTime(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime(2024, 1, 31, 23, 59)
        val mockExpenses = listOf(
            createMockExpense("1", 100.0, ExpenseCategory.FOOD, LocalDateTime(2024, 1, 15, 10, 0)),
            createMockExpense("2", 200.0, ExpenseCategory.TRANSPORTATION, LocalDateTime(2024, 1, 20, 10, 0))
        )
        fakeRepository.setExpenses(mockExpenses)
        
        viewModel.onAction(AnalyticsAction.SetCustomStartDate(startDate))
        viewModel.onAction(AnalyticsAction.SetCustomEndDate(endDate))

        // When
        viewModel.onAction(AnalyticsAction.ApplyCustomDateRange)
        advanceUntilIdle()

        // Then
        assertEquals(AnalyticsPeriod.PeriodType.CUSTOM, viewModel.state.selectedPeriod.type)
        assertEquals(startDate, viewModel.state.selectedPeriod.startDate)
        assertEquals(endDate, viewModel.state.selectedPeriod.endDate)
    }

    @Test
    fun `loadAnalyticsData sets data in state`() = runTest {
        // Given
        val mockExpenses = listOf(
            createMockExpense("1", 150.0, ExpenseCategory.FOOD),
            createMockExpense("2", 250.0, ExpenseCategory.TRANSPORTATION),
            createMockExpense("3", 100.0, ExpenseCategory.ENTERTAINMENT)
        )
        fakeRepository.setExpenses(mockExpenses)
        
        // When - Create new viewmodel to trigger init
        viewModel = AnalyticsViewModel(useCase)
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.state.analyticsData)
        assertEquals(500.0, viewModel.state.analyticsData?.totalAmount)
        assertEquals(3, viewModel.state.analyticsData?.categoryBreakdown?.size)
    }

    private fun createMockExpense(
        id: String,
        amount: Double,
        category: ExpenseCategory,
        date: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    ): Expense {
        return Expense(
            id = id,
            amount = amount,
            category = category,
            note = "Test expense",
            date = date,
            imagePath = null
        )
    }

    private fun createMockAnalyticsData(): AnalyticsData {
        return AnalyticsData(
            totalAmount = 1000.0,
            categoryBreakdown = listOf(
                CategoryData(
                    category = ExpenseCategory.FOOD,
                    amount = 500.0,
                    percentage = 50f,
                    transactionCount = 10
                ),
                CategoryData(
                    category = ExpenseCategory.TRANSPORTATION,
                    amount = 300.0,
                    percentage = 30f,
                    transactionCount = 5
                )
            ),
            monthlyTrends = emptyList(),
            dailyTrends = emptyList(),
            averagePerDay = 33.33,
            averagePerMonth = 1000.0,
            period = AnalyticsPeriod(
                startDate = LocalDateTime(2024, 1, 1, 0, 0),
                endDate = LocalDateTime(2024, 1, 31, 23, 59),
                type = AnalyticsPeriod.PeriodType.LAST_30_DAYS
            )
        )
    }
}

