package com.sulavtimsina.expensetracker.analytics.domain

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAnalyticsDataUseCaseTest {
    private val fakeRepository = FakeExpenseRepository()
    private val useCase = GetAnalyticsDataUseCase(fakeRepository)

    @Test
    fun `invoke returns correct analytics data for given period`() =
        runTest {
            // Given
            val expenses =
                listOf(
                    createExpense("1", 100.0, ExpenseCategory.FOOD, LocalDateTime(2024, 1, 1, 10, 0)),
                    createExpense("2", 50.0, ExpenseCategory.FOOD, LocalDateTime(2024, 1, 2, 10, 0)),
                    createExpense("3", 75.0, ExpenseCategory.TRANSPORTATION, LocalDateTime(2024, 1, 3, 10, 0)),
                )
            fakeRepository.setExpenses(expenses)

            val period =
                AnalyticsPeriod(
                    startDate = LocalDateTime(2024, 1, 1, 0, 0),
                    endDate = LocalDateTime(2024, 1, 31, 23, 59),
                    type = AnalyticsPeriod.PeriodType.LAST_30_DAYS,
                )

            // When
            val results = useCase(period).toList()
            val analyticsData = results.first()

            // Then
            assertEquals(225.0, analyticsData.totalAmount)
            assertEquals(2, analyticsData.categoryBreakdown.size)

            val foodCategory = analyticsData.categoryBreakdown.find { it.category == ExpenseCategory.FOOD }
            assertEquals(150.0, foodCategory?.amount)
            assertEquals(2, foodCategory?.transactionCount)
            assertEquals(66.7f, foodCategory?.percentage ?: 0f, 0.1f)

            val transportCategory = analyticsData.categoryBreakdown.find { it.category == ExpenseCategory.TRANSPORTATION }
            assertEquals(75.0, transportCategory?.amount)
            assertEquals(1, transportCategory?.transactionCount)
            assertEquals(33.3f, transportCategory?.percentage ?: 0f, 0.1f)
        }

    @Test
    fun `invoke returns empty data for no expenses`() =
        runTest {
            // Given
            fakeRepository.setExpenses(emptyList())

            val period =
                AnalyticsPeriod(
                    startDate = LocalDateTime(2024, 1, 1, 0, 0),
                    endDate = LocalDateTime(2024, 1, 31, 23, 59),
                    type = AnalyticsPeriod.PeriodType.LAST_30_DAYS,
                )

            // When
            val results = useCase(period).toList()
            val analyticsData = results.first()

            // Then
            assertEquals(0.0, analyticsData.totalAmount)
            assertTrue(analyticsData.categoryBreakdown.isEmpty())
            assertTrue(analyticsData.monthlyTrends.isEmpty())
            assertTrue(analyticsData.dailyTrends.isEmpty())
            assertEquals(0.0, analyticsData.averagePerDay)
            assertEquals(0.0, analyticsData.averagePerMonth)
        }

    @Test
    fun `invoke calculates monthly trends correctly`() =
        runTest {
            // Given
            val expenses =
                listOf(
                    createExpense("1", 100.0, ExpenseCategory.FOOD, LocalDateTime(2024, 1, 15, 10, 0)),
                    createExpense("2", 200.0, ExpenseCategory.FOOD, LocalDateTime(2024, 2, 15, 10, 0)),
                    createExpense("3", 150.0, ExpenseCategory.TRANSPORTATION, LocalDateTime(2024, 2, 20, 10, 0)),
                )
            fakeRepository.setExpenses(expenses)

            val period =
                AnalyticsPeriod(
                    startDate = LocalDateTime(2024, 1, 1, 0, 0),
                    endDate = LocalDateTime(2024, 2, 28, 23, 59),
                    type = AnalyticsPeriod.PeriodType.LAST_3_MONTHS,
                )

            // When
            val results = useCase(period).toList()
            val analyticsData = results.first()

            // Then
            assertEquals(2, analyticsData.monthlyTrends.size)

            val janData = analyticsData.monthlyTrends.find { it.year == 2024 && it.month.contains("Jan") }
            assertEquals(100.0, janData?.amount)
            assertEquals(1, janData?.transactionCount)

            val febData = analyticsData.monthlyTrends.find { it.year == 2024 && it.month.contains("Feb") }
            assertEquals(350.0, febData?.amount)
            assertEquals(2, febData?.transactionCount)
        }

    @Test
    fun `invoke calculates daily trends correctly`() =
        runTest {
            // Given
            val expenses =
                listOf(
                    createExpense("1", 100.0, ExpenseCategory.FOOD, LocalDateTime(2024, 1, 1, 10, 0)),
                    createExpense("2", 50.0, ExpenseCategory.FOOD, LocalDateTime(2024, 1, 1, 15, 0)),
                    createExpense("3", 75.0, ExpenseCategory.TRANSPORTATION, LocalDateTime(2024, 1, 2, 10, 0)),
                )
            fakeRepository.setExpenses(expenses)

            val period =
                AnalyticsPeriod(
                    startDate = LocalDateTime(2024, 1, 1, 0, 0),
                    endDate = LocalDateTime(2024, 1, 2, 23, 59),
                    type = AnalyticsPeriod.PeriodType.LAST_30_DAYS,
                )

            // When
            val results = useCase(period).toList()
            val analyticsData = results.first()

            // Then
            assertEquals(2, analyticsData.dailyTrends.size)

            val day1Data = analyticsData.dailyTrends.find { it.date.dayOfMonth == 1 }
            assertEquals(150.0, day1Data?.amount)
            assertEquals(2, day1Data?.transactionCount)

            val day2Data = analyticsData.dailyTrends.find { it.date.dayOfMonth == 2 }
            assertEquals(75.0, day2Data?.amount)
            assertEquals(1, day2Data?.transactionCount)
        }

    private fun createExpense(
        id: String,
        amount: Double,
        category: ExpenseCategory,
        date: LocalDateTime,
    ): Expense {
        return Expense(
            id = id,
            amount = amount,
            category = category,
            note = null,
            date = date,
            imagePath = null,
        )
    }
}

class FakeExpenseRepository : ExpenseRepository {
    private var expenses = listOf<Expense>()

    fun setExpenses(expenses: List<Expense>) {
        this.expenses = expenses
    }

    override fun getAllExpenses() = flowOf(expenses)

    override suspend fun getExpenseById(id: String) =
        com.sulavtimsina.expensetracker.core.domain.Result.Success(expenses.find { it.id == id })

    override suspend fun insertExpense(expense: Expense) = com.sulavtimsina.expensetracker.core.domain.Result.Success(Unit)

    override suspend fun updateExpense(expense: Expense) = com.sulavtimsina.expensetracker.core.domain.Result.Success(Unit)

    override suspend fun deleteExpense(id: String) = com.sulavtimsina.expensetracker.core.domain.Result.Success(Unit)

    override fun getExpensesByCategory(category: ExpenseCategory) = flowOf(expenses.filter { it.category == category })

    override fun getExpensesByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ) = flowOf(expenses.filter { it.date >= startDate && it.date <= endDate })
}
