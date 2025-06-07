package com.plcoding.expensetracker.analytics.presentation

import com.plcoding.expensetracker.analytics.domain.AnalyticsData
import com.plcoding.expensetracker.analytics.domain.AnalyticsPeriod
import com.plcoding.expensetracker.analytics.domain.CategoryData
import com.plcoding.expensetracker.analytics.domain.GetAnalyticsDataUseCase
import com.plcoding.expensetracker.expense.domain.ExpenseCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import kotlinx.datetime.LocalDateTime
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeUseCase: FakeGetAnalyticsDataUseCase
    private lateinit var viewModel: AnalyticsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeUseCase = FakeGetAnalyticsDataUseCase()
        viewModel = AnalyticsViewModel(fakeUseCase)
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
        val mockData = createMockAnalyticsData()
        fakeUseCase.setReturnValue(mockData)

        // When
        viewModel.onAction(AnalyticsAction.SelectPeriodType(AnalyticsPeriod.PeriodType.LAST_3_MONTHS))

        // Then
        assertEquals(AnalyticsPeriod.PeriodType.LAST_3_MONTHS, viewModel.state.selectedPeriod.type)
        assertEquals(mockData, viewModel.state.analyticsData)
        assertFalse(viewModel.state.isLoading)
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
    fun `onAction HideDatePicker hides date picker`() {
        // Given
        viewModel.onAction(AnalyticsAction.ShowDatePicker(AnalyticsState.DatePickerType.START_DATE))

        // When
        viewModel.onAction(AnalyticsAction.HideDatePicker)

        // Then
        assertFalse(viewModel.state.showDatePicker)
        assertNull(viewModel.state.datePickerType)
    }

    @Test
    fun `onAction SetCustomStartDate updates custom start date`() {
        // Given
        val customDate = LocalDateTime(2024, 1, 1, 0, 0)

        // When
        viewModel.onAction(AnalyticsAction.SetCustomStartDate(customDate))

        // Then
        assertEquals(customDate, viewModel.state.customStartDate)
    }

    @Test
    fun `onAction SetCustomEndDate updates custom end date`() {
        // Given
        val customDate = LocalDateTime(2024, 1, 31, 23, 59)

        // When
        viewModel.onAction(AnalyticsAction.SetCustomEndDate(customDate))

        // Then
        assertEquals(customDate, viewModel.state.customEndDate)
    }

    @Test
    fun `onAction ApplyCustomDateRange applies custom period when dates are valid`() = runTest {
        // Given
        val startDate = LocalDateTime(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime(2024, 1, 31, 23, 59)
        val mockData = createMockAnalyticsData()
        fakeUseCase.setReturnValue(mockData)

        viewModel.onAction(AnalyticsAction.SetCustomStartDate(startDate))
        viewModel.onAction(AnalyticsAction.SetCustomEndDate(endDate))

        // When
        viewModel.onAction(AnalyticsAction.ApplyCustomDateRange)

        // Then
        assertEquals(AnalyticsPeriod.PeriodType.CUSTOM, viewModel.state.selectedPeriod.type)
        assertEquals(startDate, viewModel.state.selectedPeriod.startDate)
        assertEquals(endDate, viewModel.state.selectedPeriod.endDate)
    }

    @Test
    fun `onAction ApplyCustomDateRange does not apply when start date is after end date`() {
        // Given
        val startDate = LocalDateTime(2024, 1, 31, 0, 0)
        val endDate = LocalDateTime(2024, 1, 1, 23, 59)
        val originalPeriod = viewModel.state.selectedPeriod

        viewModel.onAction(AnalyticsAction.SetCustomStartDate(startDate))
        viewModel.onAction(AnalyticsAction.SetCustomEndDate(endDate))

        // When
        viewModel.onAction(AnalyticsAction.ApplyCustomDateRange)

        // Then
        assertEquals(originalPeriod, viewModel.state.selectedPeriod)
    }

    @Test
    fun `onAction RefreshData reloads analytics data`() = runTest {
        // Given
        val mockData = createMockAnalyticsData()
        fakeUseCase.setReturnValue(mockData)

        // When
        viewModel.onAction(AnalyticsAction.RefreshData)

        // Then
        assertEquals(mockData, viewModel.state.analyticsData)
        assertFalse(viewModel.state.isLoading)
    }

    private fun createMockAnalyticsData(): AnalyticsData {
        return AnalyticsData(
            totalAmount = 500.0,
            categoryBreakdown = listOf(
                CategoryData(
                    category = ExpenseCategory.FOOD,
                    amount = 300.0,
                    percentage = 60f,
                    transactionCount = 5
                ),
                CategoryData(
                    category = ExpenseCategory.TRANSPORTATION,
                    amount = 200.0,
                    percentage = 40f,
                    transactionCount = 3
                )
            ),
            monthlyTrends = emptyList(),
            dailyTrends = emptyList(),
            averagePerDay = 16.67,
            averagePerMonth = 500.0,
            period = AnalyticsPeriod(
                startDate = LocalDateTime(2024, 1, 1, 0, 0),
                endDate = LocalDateTime(2024, 1, 31, 23, 59),
                type = AnalyticsPeriod.PeriodType.LAST_30_DAYS
            )
        )
    }
}

class FakeGetAnalyticsDataUseCase : GetAnalyticsDataUseCase(
    repository = object : com.plcoding.expensetracker.expense.domain.ExpenseRepository {
        override fun getAllExpenses() = flowOf(emptyList<com.plcoding.expensetracker.expense.domain.Expense>())
        override suspend fun getExpenseById(id: String) = com.plcoding.expensetracker.core.domain.Result.Success(null)
        override suspend fun insertExpense(expense: com.plcoding.expensetracker.expense.domain.Expense) = com.plcoding.expensetracker.core.domain.Result.Success(Unit)
        override suspend fun updateExpense(expense: com.plcoding.expensetracker.expense.domain.Expense) = com.plcoding.expensetracker.core.domain.Result.Success(Unit)
        override suspend fun deleteExpense(id: String) = com.plcoding.expensetracker.core.domain.Result.Success(Unit)
        override fun getExpensesByCategory(category: ExpenseCategory) = flowOf(emptyList())
        override fun getExpensesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) = flowOf(emptyList())
    }
) {
    private var returnValue: AnalyticsData? = null

    fun setReturnValue(data: AnalyticsData) {
        returnValue = data
    }

    override fun invoke(period: AnalyticsPeriod) = flowOf(
        returnValue ?: AnalyticsData(
            totalAmount = 0.0,
            categoryBreakdown = emptyList(),
            monthlyTrends = emptyList(),
            dailyTrends = emptyList(),
            averagePerDay = 0.0,
            averagePerMonth = 0.0,
            period = period
        )
    )
}
