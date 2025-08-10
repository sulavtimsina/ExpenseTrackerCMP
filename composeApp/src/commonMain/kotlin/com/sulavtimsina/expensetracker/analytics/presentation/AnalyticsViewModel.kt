package com.sulavtimsina.expensetracker.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sulavtimsina.expensetracker.analytics.domain.AnalyticsPeriod
import com.sulavtimsina.expensetracker.analytics.domain.GetAnalyticsDataUseCase
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AnalyticsViewModel(
    private val getAnalyticsDataUseCase: GetAnalyticsDataUseCase,
) : ViewModel() {
    var state by mutableStateOf(AnalyticsState())
        private set

    init {
        loadAnalyticsData()
    }

    fun onAction(action: AnalyticsAction) {
        when (action) {
            is AnalyticsAction.SelectPeriodType -> {
                val newPeriod = createPeriodFromType(action.periodType)
                state = state.copy(selectedPeriod = newPeriod)
                loadAnalyticsData()
            }
            is AnalyticsAction.SelectChartType -> {
                state = state.copy(selectedChartType = action.chartType)
            }
            is AnalyticsAction.ShowDatePicker -> {
                state =
                    state.copy(
                        showDatePicker = true,
                        datePickerType = action.datePickerType,
                    )
            }
            AnalyticsAction.HideDatePicker -> {
                state =
                    state.copy(
                        showDatePicker = false,
                        datePickerType = null,
                    )
            }
            is AnalyticsAction.SetCustomStartDate -> {
                state = state.copy(customStartDate = action.date)
            }
            is AnalyticsAction.SetCustomEndDate -> {
                state = state.copy(customEndDate = action.date)
            }
            AnalyticsAction.ApplyCustomDateRange -> {
                val startDate = state.customStartDate
                val endDate = state.customEndDate
                if (startDate != null && endDate != null && startDate <= endDate) {
                    val customPeriod =
                        AnalyticsPeriod(
                            startDate = startDate,
                            endDate = endDate,
                            type = AnalyticsPeriod.PeriodType.CUSTOM,
                        )
                    state = state.copy(selectedPeriod = customPeriod)
                    loadAnalyticsData()
                }
            }
            AnalyticsAction.RefreshData -> {
                loadAnalyticsData()
            }
        }
    }

    private fun loadAnalyticsData() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            getAnalyticsDataUseCase(state.selectedPeriod)
                .collect { analyticsData ->
                    state =
                        state.copy(
                            analyticsData = analyticsData,
                            isLoading = false,
                        )
                }
        }
    }

    private fun createPeriodFromType(periodType: AnalyticsPeriod.PeriodType): AnalyticsPeriod {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val endDate = LocalDateTime(now.year, now.month, now.dayOfMonth, 23, 59)

        val startDate =
            when (periodType) {
                AnalyticsPeriod.PeriodType.LAST_30_DAYS -> {
                    subtractDays(endDate, 30)
                }
                AnalyticsPeriod.PeriodType.LAST_3_MONTHS -> {
                    subtractMonths(endDate, 3)
                }
                AnalyticsPeriod.PeriodType.LAST_6_MONTHS -> {
                    subtractMonths(endDate, 6)
                }
                AnalyticsPeriod.PeriodType.LAST_YEAR -> {
                    subtractMonths(endDate, 12)
                }
                AnalyticsPeriod.PeriodType.CUSTOM -> {
                    // Should not reach here for non-custom types
                    endDate
                }
            }

        return AnalyticsPeriod(
            startDate = startDate,
            endDate = endDate,
            type = periodType,
        )
    }

    private fun subtractDays(
        date: LocalDateTime,
        days: Int,
    ): LocalDateTime {
        // Simple implementation - for production use kotlinx-datetime date arithmetic
        val epochDays = date.date.toEpochDays()
        val newEpochDays = epochDays - days
        val newDate = kotlinx.datetime.LocalDate.fromEpochDays(newEpochDays.toInt())
        return LocalDateTime(newDate.year, newDate.month, newDate.dayOfMonth, 0, 0)
    }

    private fun subtractMonths(
        date: LocalDateTime,
        months: Int,
    ): LocalDateTime {
        var year = date.year
        var month = date.monthNumber - months

        while (month <= 0) {
            month += 12
            year--
        }

        val adjustedMonth = kotlinx.datetime.Month(month)
        return LocalDateTime(year, adjustedMonth, 1, 0, 0)
    }
}
