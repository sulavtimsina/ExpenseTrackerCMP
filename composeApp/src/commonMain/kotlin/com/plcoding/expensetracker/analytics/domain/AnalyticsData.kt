package com.plcoding.expensetracker.analytics.domain

import com.plcoding.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime

data class AnalyticsData(
    val totalAmount: Double,
    val categoryBreakdown: List<CategoryData>,
    val monthlyTrends: List<MonthlyData>,
    val dailyTrends: List<DailyData>,
    val averagePerDay: Double,
    val averagePerMonth: Double,
    val period: AnalyticsPeriod
)

data class CategoryData(
    val category: ExpenseCategory,
    val amount: Double,
    val percentage: Float,
    val transactionCount: Int
)

data class MonthlyData(
    val month: String,
    val year: Int,
    val amount: Double,
    val transactionCount: Int
)

data class DailyData(
    val date: LocalDateTime,
    val amount: Double,
    val transactionCount: Int
)

data class AnalyticsPeriod(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val type: PeriodType
) {
    enum class PeriodType {
        LAST_30_DAYS,
        LAST_3_MONTHS,
        LAST_6_MONTHS,
        LAST_YEAR,
        CUSTOM
    }
}
