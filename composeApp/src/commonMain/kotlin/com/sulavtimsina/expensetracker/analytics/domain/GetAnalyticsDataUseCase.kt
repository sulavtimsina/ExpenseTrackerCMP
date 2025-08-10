package com.sulavtimsina.expensetracker.analytics.domain

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames

class GetAnalyticsDataUseCase(
    private val repository: ExpenseRepository,
) {
    operator fun invoke(period: AnalyticsPeriod): Flow<AnalyticsData> {
        return repository.getExpensesByDateRange(period.startDate, period.endDate)
            .map { expenses ->
                calculateAnalyticsData(expenses, period)
            }
    }

    private fun calculateAnalyticsData(
        expenses: List<Expense>,
        period: AnalyticsPeriod,
    ): AnalyticsData {
        val totalAmount = expenses.sumOf { it.amount }

        val categoryBreakdown = calculateCategoryBreakdown(expenses, totalAmount)
        val monthlyTrends = calculateMonthlyTrends(expenses)
        val dailyTrends = calculateDailyTrends(expenses)

        val totalDays = calculateDaysBetween(period.startDate, period.endDate)
        val totalMonths = calculateMonthsBetween(period.startDate, period.endDate)

        val averagePerDay = if (totalDays > 0) totalAmount / totalDays else 0.0
        val averagePerMonth = if (totalMonths > 0) totalAmount / totalMonths else 0.0

        return AnalyticsData(
            totalAmount = totalAmount,
            categoryBreakdown = categoryBreakdown,
            monthlyTrends = monthlyTrends,
            dailyTrends = dailyTrends,
            averagePerDay = averagePerDay,
            averagePerMonth = averagePerMonth,
            period = period,
        )
    }

    private fun calculateCategoryBreakdown(
        expenses: List<Expense>,
        totalAmount: Double,
    ): List<CategoryData> {
        return expenses
            .groupBy { it.category }
            .map { (category, categoryExpenses) ->
                val amount = categoryExpenses.sumOf { it.amount }
                val percentage = if (totalAmount > 0) (amount / totalAmount * 100).toFloat() else 0f
                CategoryData(
                    category = category,
                    amount = amount,
                    percentage = percentage,
                    transactionCount = categoryExpenses.size,
                )
            }
            .sortedByDescending { it.amount }
    }

    private fun calculateMonthlyTrends(expenses: List<Expense>): List<MonthlyData> {
        return expenses
            .groupBy { "${it.date.year}-${it.date.monthNumber.toString().padStart(2, '0')}" }
            .map { (monthKey, monthExpenses) ->
                val firstExpense = monthExpenses.first()
                val monthFormat =
                    LocalDateTime.Format {
                        monthName(MonthNames.ENGLISH_ABBREVIATED)
                    }
                MonthlyData(
                    month = firstExpense.date.format(monthFormat),
                    year = firstExpense.date.year,
                    amount = monthExpenses.sumOf { it.amount },
                    transactionCount = monthExpenses.size,
                )
            }
            .sortedBy { "${it.year}-${getMonthNumber(it.month)}" }
    }

    private fun calculateDailyTrends(expenses: List<Expense>): List<DailyData> {
        return expenses
            .groupBy { "${it.date.year}-${it.date.monthNumber}-${it.date.dayOfMonth}" }
            .map { (_, dayExpenses) ->
                val firstExpense = dayExpenses.first()
                DailyData(
                    date =
                        LocalDateTime(
                            firstExpense.date.year,
                            firstExpense.date.month,
                            firstExpense.date.dayOfMonth,
                            0,
                            0,
                        ),
                    amount = dayExpenses.sumOf { it.amount },
                    transactionCount = dayExpenses.size,
                )
            }
            .sortedBy { it.date }
    }

    private fun calculateDaysBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Int {
        // Simple day calculation - for more accuracy, use kotlinx-datetime's date arithmetic
        val startEpochDays = startDate.date.toEpochDays()
        val endEpochDays = endDate.date.toEpochDays()
        return (endEpochDays - startEpochDays).toInt().coerceAtLeast(1)
    }

    private fun calculateMonthsBetween(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Int {
        val monthDiff =
            (endDate.year - startDate.year) * 12 +
                (endDate.monthNumber - startDate.monthNumber)
        return monthDiff.coerceAtLeast(1)
    }

    private fun getMonthNumber(monthName: String): String {
        return when (monthName.lowercase()) {
            "jan" -> "01"
            "feb" -> "02"
            "mar" -> "03"
            "apr" -> "04"
            "may" -> "05"
            "jun" -> "06"
            "jul" -> "07"
            "aug" -> "08"
            "sep" -> "09"
            "oct" -> "10"
            "nov" -> "11"
            "dec" -> "12"
            else -> "01"
        }
    }
}
