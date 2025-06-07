package com.plcoding.expensetracker.analytics.domain

import com.plcoding.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime
import kotlin.test.*

class AnalyticsDataTest {

    @Test
    fun `AnalyticsData creates correctly with all properties`() {
        // Given
        val categoryData = listOf(
            CategoryData(
                category = ExpenseCategory.FOOD,
                amount = 100.0,
                percentage = 50f,
                transactionCount = 2
            )
        )
        val monthlyData = listOf(
            MonthlyData(
                month = "Jan",
                year = 2024,
                amount = 200.0,
                transactionCount = 3
            )
        )
        val dailyData = listOf(
            DailyData(
                date = LocalDateTime(2024, 1, 1, 0, 0),
                amount = 50.0,
                transactionCount = 1
            )
        )
        val period = AnalyticsPeriod(
            startDate = LocalDateTime(2024, 1, 1, 0, 0),
            endDate = LocalDateTime(2024, 1, 31, 23, 59),
            type = AnalyticsPeriod.PeriodType.LAST_30_DAYS
        )

        // When
        val analyticsData = AnalyticsData(
            totalAmount = 200.0,
            categoryBreakdown = categoryData,
            monthlyTrends = monthlyData,
            dailyTrends = dailyData,
            averagePerDay = 6.45,
            averagePerMonth = 200.0,
            period = period
        )

        // Then
        assertEquals(200.0, analyticsData.totalAmount)
        assertEquals(categoryData, analyticsData.categoryBreakdown)
        assertEquals(monthlyData, analyticsData.monthlyTrends)
        assertEquals(dailyData, analyticsData.dailyTrends)
        assertEquals(6.45, analyticsData.averagePerDay)
        assertEquals(200.0, analyticsData.averagePerMonth)
        assertEquals(period, analyticsData.period)
    }

    @Test
    fun `CategoryData calculates percentage correctly`() {
        // Given/When
        val categoryData = CategoryData(
            category = ExpenseCategory.FOOD,
            amount = 150.0,
            percentage = 75f,
            transactionCount = 5
        )

        // Then
        assertEquals(ExpenseCategory.FOOD, categoryData.category)
        assertEquals(150.0, categoryData.amount)
        assertEquals(75f, categoryData.percentage)
        assertEquals(5, categoryData.transactionCount)
    }

    @Test
    fun `MonthlyData stores month and year correctly`() {
        // Given/When
        val monthlyData = MonthlyData(
            month = "Dec",
            year = 2023,
            amount = 1250.75,
            transactionCount = 15
        )

        // Then
        assertEquals("Dec", monthlyData.month)
        assertEquals(2023, monthlyData.year)
        assertEquals(1250.75, monthlyData.amount)
        assertEquals(15, monthlyData.transactionCount)
    }

    @Test
    fun `DailyData stores date and amounts correctly`() {
        // Given
        val date = LocalDateTime(2024, 5, 15, 14, 30)
        
        // When
        val dailyData = DailyData(
            date = date,
            amount = 85.50,
            transactionCount = 3
        )

        // Then
        assertEquals(date, dailyData.date)
        assertEquals(85.50, dailyData.amount)
        assertEquals(3, dailyData.transactionCount)
    }

    @Test
    fun `AnalyticsPeriod creates correctly for different types`() {
        // Given
        val startDate = LocalDateTime(2024, 1, 1, 0, 0)
        val endDate = LocalDateTime(2024, 3, 31, 23, 59)

        // When
        val period = AnalyticsPeriod(
            startDate = startDate,
            endDate = endDate,
            type = AnalyticsPeriod.PeriodType.LAST_3_MONTHS
        )

        // Then
        assertEquals(startDate, period.startDate)
        assertEquals(endDate, period.endDate)
        assertEquals(AnalyticsPeriod.PeriodType.LAST_3_MONTHS, period.type)
    }

    @Test
    fun `AnalyticsPeriod PeriodType enum contains all expected values`() {
        // When/Then
        val periodTypes = AnalyticsPeriod.PeriodType.entries
        
        assertTrue(periodTypes.contains(AnalyticsPeriod.PeriodType.LAST_30_DAYS))
        assertTrue(periodTypes.contains(AnalyticsPeriod.PeriodType.LAST_3_MONTHS))
        assertTrue(periodTypes.contains(AnalyticsPeriod.PeriodType.LAST_6_MONTHS))
        assertTrue(periodTypes.contains(AnalyticsPeriod.PeriodType.LAST_YEAR))
        assertTrue(periodTypes.contains(AnalyticsPeriod.PeriodType.CUSTOM))
        assertEquals(5, periodTypes.size)
    }

    @Test
    fun `CategoryData with zero amount has zero percentage`() {
        // Given/When
        val categoryData = CategoryData(
            category = ExpenseCategory.OTHER,
            amount = 0.0,
            percentage = 0f,
            transactionCount = 0
        )

        // Then
        assertEquals(0.0, categoryData.amount)
        assertEquals(0f, categoryData.percentage)
        assertEquals(0, categoryData.transactionCount)
    }

    @Test
    fun `AnalyticsData with empty lists represents no data state`() {
        // Given
        val period = AnalyticsPeriod(
            startDate = LocalDateTime(2024, 1, 1, 0, 0),
            endDate = LocalDateTime(2024, 1, 31, 23, 59),
            type = AnalyticsPeriod.PeriodType.LAST_30_DAYS
        )

        // When
        val analyticsData = AnalyticsData(
            totalAmount = 0.0,
            categoryBreakdown = emptyList(),
            monthlyTrends = emptyList(),
            dailyTrends = emptyList(),
            averagePerDay = 0.0,
            averagePerMonth = 0.0,
            period = period
        )

        // Then
        assertEquals(0.0, analyticsData.totalAmount)
        assertTrue(analyticsData.categoryBreakdown.isEmpty())
        assertTrue(analyticsData.monthlyTrends.isEmpty())
        assertTrue(analyticsData.dailyTrends.isEmpty())
        assertEquals(0.0, analyticsData.averagePerDay)
        assertEquals(0.0, analyticsData.averagePerMonth)
    }
}
