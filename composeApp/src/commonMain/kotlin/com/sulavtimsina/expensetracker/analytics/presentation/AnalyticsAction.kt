package com.sulavtimsina.expensetracker.analytics.presentation

import com.sulavtimsina.expensetracker.analytics.domain.AnalyticsPeriod
import kotlinx.datetime.LocalDateTime

sealed interface AnalyticsAction {
    data class SelectPeriodType(val periodType: AnalyticsPeriod.PeriodType) : AnalyticsAction

    data class SelectChartType(val chartType: AnalyticsState.ChartType) : AnalyticsAction

    data class ShowDatePicker(val datePickerType: AnalyticsState.DatePickerType) : AnalyticsAction

    data object HideDatePicker : AnalyticsAction

    data class SetCustomStartDate(val date: LocalDateTime) : AnalyticsAction

    data class SetCustomEndDate(val date: LocalDateTime) : AnalyticsAction

    data object ApplyCustomDateRange : AnalyticsAction

    data object RefreshData : AnalyticsAction
}
