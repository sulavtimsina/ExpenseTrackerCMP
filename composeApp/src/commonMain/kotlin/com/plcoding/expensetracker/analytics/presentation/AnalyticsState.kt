package com.plcoding.expensetracker.analytics.presentation

import com.plcoding.expensetracker.analytics.domain.AnalyticsData
import com.plcoding.expensetracker.analytics.domain.AnalyticsPeriod
import kotlinx.datetime.LocalDateTime

data class AnalyticsState(
    val analyticsData: AnalyticsData? = null,
    val isLoading: Boolean = false,
    val selectedPeriod: AnalyticsPeriod = getDefaultPeriod(),
    val selectedChartType: ChartType = ChartType.CATEGORY_PIE,
    val customStartDate: LocalDateTime? = null,
    val customEndDate: LocalDateTime? = null,
    val showDatePicker: Boolean = false,
    val datePickerType: DatePickerType? = null
) {
    enum class ChartType {
        CATEGORY_PIE,
        MONTHLY_TREND,
        DAILY_TREND
    }

    enum class DatePickerType {
        START_DATE,
        END_DATE
    }
}

private fun getDefaultPeriod(): AnalyticsPeriod {
    val now = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
    val thirtyDaysAgo = now.let {
        LocalDateTime(it.year, it.month, it.dayOfMonth, 0, 0).let { date ->
            if (date.dayOfMonth > 30) {
                LocalDateTime(date.year, date.month.minus(1), date.dayOfMonth - 30, 0, 0)
            } else if (date.dayOfMonth <= 30) {
                LocalDateTime(date.year, date.month.minus(1), 30 - date.dayOfMonth + 1, 0, 0)
            } else {
                date.let { d ->
                    LocalDateTime(d.year, d.month.minus(1), d.dayOfMonth, 0, 0)
                }
            }
        }
    }
    
    return AnalyticsPeriod(
        startDate = thirtyDaysAgo,
        endDate = now,
        type = AnalyticsPeriod.PeriodType.LAST_30_DAYS
    )
}
