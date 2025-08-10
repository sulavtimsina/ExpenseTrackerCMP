package com.sulavtimsina.expensetracker.analytics.presentation

import com.sulavtimsina.expensetracker.analytics.domain.AnalyticsData
import com.sulavtimsina.expensetracker.analytics.domain.AnalyticsPeriod
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class AnalyticsState(
    val analyticsData: AnalyticsData? = null,
    val isLoading: Boolean = false,
    val selectedPeriod: AnalyticsPeriod = getDefaultPeriod(),
    val selectedChartType: ChartType = ChartType.CATEGORY_PIE,
    val customStartDate: LocalDateTime? = null,
    val customEndDate: LocalDateTime? = null,
    val showDatePicker: Boolean = false,
    val datePickerType: DatePickerType? = null,
) {
    enum class ChartType {
        CATEGORY_PIE,
        MONTHLY_TREND,
        DAILY_TREND,
    }

    enum class DatePickerType {
        START_DATE,
        END_DATE,
    }
}

private fun getDefaultPeriod(): AnalyticsPeriod {
    val instant = kotlinx.datetime.Clock.System.now()
    val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
    val now = instant.toLocalDateTime(timeZone)
    val thirtyDaysAgo =
        kotlinx.datetime.LocalDateTime(
            now.year,
            now.month,
            (now.dayOfMonth - 30).coerceAtLeast(1),
            0,
            0,
        )

    return AnalyticsPeriod(
        startDate = thirtyDaysAgo,
        endDate = now,
        type = AnalyticsPeriod.PeriodType.LAST_30_DAYS,
    )
}
