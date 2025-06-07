package com.plcoding.expensetracker.analytics.presentation

import com.plcoding.expensetracker.analytics.domain.AnalyticsPeriod
import kotlin.test.*

class AnalyticsStateTest {

    @Test
    fun `AnalyticsState default values are correct`() {
        // When
        val state = AnalyticsState()

        // Then
        assertNull(state.analyticsData)
        assertFalse(state.isLoading)
        assertEquals(AnalyticsPeriod.PeriodType.LAST_30_DAYS, state.selectedPeriod.type)
        assertEquals(AnalyticsState.ChartType.CATEGORY_PIE, state.selectedChartType)
        assertNull(state.customStartDate)
        assertNull(state.customEndDate)
        assertFalse(state.showDatePicker)
        assertNull(state.datePickerType)
    }

    @Test
    fun `AnalyticsState can be updated with new values`() {
        // Given
        val initialState = AnalyticsState()

        // When
        val updatedState = initialState.copy(
            isLoading = true,
            selectedChartType = AnalyticsState.ChartType.MONTHLY_TREND,
            showDatePicker = true,
            datePickerType = AnalyticsState.DatePickerType.START_DATE
        )

        // Then
        assertTrue(updatedState.isLoading)
        assertEquals(AnalyticsState.ChartType.MONTHLY_TREND, updatedState.selectedChartType)
        assertTrue(updatedState.showDatePicker)
        assertEquals(AnalyticsState.DatePickerType.START_DATE, updatedState.datePickerType)
    }

    @Test
    fun `ChartType enum contains all expected values`() {
        // When/Then
        val chartTypes = AnalyticsState.ChartType.entries
        
        assertTrue(chartTypes.contains(AnalyticsState.ChartType.CATEGORY_PIE))
        assertTrue(chartTypes.contains(AnalyticsState.ChartType.MONTHLY_TREND))
        assertTrue(chartTypes.contains(AnalyticsState.ChartType.DAILY_TREND))
        assertEquals(3, chartTypes.size)
    }

    @Test
    fun `DatePickerType enum contains all expected values`() {
        // When/Then
        val datePickerTypes = AnalyticsState.DatePickerType.entries
        
        assertTrue(datePickerTypes.contains(AnalyticsState.DatePickerType.START_DATE))
        assertTrue(datePickerTypes.contains(AnalyticsState.DatePickerType.END_DATE))
        assertEquals(2, datePickerTypes.size)
    }

    @Test
    fun `AnalyticsState with custom dates`() {
        // Given
        val customStartDate = kotlinx.datetime.LocalDateTime(2024, 1, 1, 0, 0)
        val customEndDate = kotlinx.datetime.LocalDateTime(2024, 1, 31, 23, 59)

        // When
        val state = AnalyticsState(
            customStartDate = customStartDate,
            customEndDate = customEndDate
        )

        // Then
        assertEquals(customStartDate, state.customStartDate)
        assertEquals(customEndDate, state.customEndDate)
    }
}
