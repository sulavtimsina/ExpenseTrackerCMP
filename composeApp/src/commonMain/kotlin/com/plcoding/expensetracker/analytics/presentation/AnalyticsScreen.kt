package com.plcoding.expensetracker.analytics.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.expensetracker.analytics.domain.AnalyticsPeriod
import com.plcoding.expensetracker.analytics.presentation.components.LineChart
import com.plcoding.expensetracker.analytics.presentation.components.PieChart
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.round

// Multiplatform-compatible number formatting
private fun Double.formatCurrency(): String = 
    "${(round(this * 100) / 100.0)}"

private fun Double.formatPercentage(): String = 
    "${(round(this * 10) / 10.0)}"

@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalyticsViewModel = koinViewModel()
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onAction(AnalyticsAction.RefreshData) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Period Selection
            item {
                PeriodSelectionSection(
                    selectedPeriod = state.selectedPeriod,
                    onPeriodSelected = { periodType ->
                        viewModel.onAction(AnalyticsAction.SelectPeriodType(periodType))
                    },
                    onCustomDateClick = {
                        viewModel.onAction(AnalyticsAction.ShowDatePicker(AnalyticsState.DatePickerType.START_DATE))
                    }
                )
            }

            if (state.isLoading) {
                item {
                    LoadingSection()
                }
            } else {
                state.analyticsData?.let { data ->
                    // Summary Cards
                    item {
                        SummarySection(data)
                    }

                    // Chart Type Selection
                    item {
                        ChartTypeSelection(
                            selectedChartType = state.selectedChartType,
                            onChartTypeSelected = { chartType ->
                                viewModel.onAction(AnalyticsAction.SelectChartType(chartType))
                            }
                        )
                    }

                    // Charts
                    item {
                        when (state.selectedChartType) {
                            AnalyticsState.ChartType.CATEGORY_PIE -> {
                                if (data.categoryBreakdown.isNotEmpty()) {
                                    PieChart(
                                        data = data.categoryBreakdown,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    NoDataMessage("No spending data available for this period")
                                }
                            }
                            AnalyticsState.ChartType.MONTHLY_TREND -> {
                                LineChart(
                                    monthlyData = data.monthlyTrends,
                                    dailyData = data.dailyTrends,
                                    showMonthly = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            AnalyticsState.ChartType.DAILY_TREND -> {
                                LineChart(
                                    monthlyData = data.monthlyTrends,
                                    dailyData = data.dailyTrends,
                                    showMonthly = false,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                } ?: run {
                    item {
                        NoDataMessage("No data available for the selected period")
                    }
                }
            }
        }
    }

    // Custom Date Picker Dialog (simplified)
    if (state.showDatePicker) {
        DatePickerDialog(
            datePickerType = state.datePickerType!!,
            onDateSelected = { date ->
                when (state.datePickerType) {
                    AnalyticsState.DatePickerType.START_DATE -> {
                        viewModel.onAction(AnalyticsAction.SetCustomStartDate(date))
                    }
                    AnalyticsState.DatePickerType.END_DATE -> {
                        viewModel.onAction(AnalyticsAction.SetCustomEndDate(date))
                    }
                    null -> {}
                }
                viewModel.onAction(AnalyticsAction.HideDatePicker)
            },
            onDismiss = {
                viewModel.onAction(AnalyticsAction.HideDatePicker)
            }
        )
    }
}

@Composable
private fun PeriodSelectionSection(
    selectedPeriod: AnalyticsPeriod,
    onPeriodSelected: (AnalyticsPeriod.PeriodType) -> Unit,
    onCustomDateClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Time Period",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodChip(
                    text = "30 Days",
                    isSelected = selectedPeriod.type == AnalyticsPeriod.PeriodType.LAST_30_DAYS,
                    onClick = { onPeriodSelected(AnalyticsPeriod.PeriodType.LAST_30_DAYS) },
                    modifier = Modifier.weight(1f)
                )
                PeriodChip(
                    text = "3 Months",
                    isSelected = selectedPeriod.type == AnalyticsPeriod.PeriodType.LAST_3_MONTHS,
                    onClick = { onPeriodSelected(AnalyticsPeriod.PeriodType.LAST_3_MONTHS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodChip(
                    text = "6 Months",
                    isSelected = selectedPeriod.type == AnalyticsPeriod.PeriodType.LAST_6_MONTHS,
                    onClick = { onPeriodSelected(AnalyticsPeriod.PeriodType.LAST_6_MONTHS) },
                    modifier = Modifier.weight(1f)
                )
                PeriodChip(
                    text = "1 Year",
                    isSelected = selectedPeriod.type == AnalyticsPeriod.PeriodType.LAST_YEAR,
                    onClick = { onPeriodSelected(AnalyticsPeriod.PeriodType.LAST_YEAR) },
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = onCustomDateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Custom Date Range")
            }
        }
    }
}

@Composable
private fun PeriodChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )
    ) {
        Text(text)
    }
}

@Composable
private fun SummarySection(data: com.plcoding.expensetracker.analytics.domain.AnalyticsData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem(
                    title = "Total Spent",
                    value = "$${data.totalAmount.formatCurrency()}",
                    modifier = Modifier.weight(1f)
                )
                SummaryItem(
                    title = "Avg/Day",
                    value = "$${data.averagePerDay.formatCurrency()}",
                    modifier = Modifier.weight(1f)
                )
                SummaryItem(
                    title = "Avg/Month",
                    value = "$${data.averagePerMonth.formatCurrency()}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun ChartTypeSelection(
    selectedChartType: AnalyticsState.ChartType,
    onChartTypeSelected: (AnalyticsState.ChartType) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Chart View",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onChartTypeSelected(AnalyticsState.ChartType.CATEGORY_PIE) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedChartType == AnalyticsState.ChartType.CATEGORY_PIE) 
                            MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                ) {
                    Text("Categories")
                }
                Button(
                    onClick = { onChartTypeSelected(AnalyticsState.ChartType.MONTHLY_TREND) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedChartType == AnalyticsState.ChartType.MONTHLY_TREND) 
                            MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                ) {
                    Text("Monthly")
                }
                Button(
                    onClick = { onChartTypeSelected(AnalyticsState.ChartType.DAILY_TREND) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedChartType == AnalyticsState.ChartType.DAILY_TREND) 
                            MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    )
                ) {
                    Text("Daily")
                }
            }
        }
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun NoDataMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun DatePickerDialog(
    datePickerType: AnalyticsState.DatePickerType,
    onDateSelected: (kotlinx.datetime.LocalDateTime) -> Unit,
    onDismiss: () -> Unit
) {
    // Simplified date picker - in production use Material3 DatePicker
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = when (datePickerType) {
                    AnalyticsState.DatePickerType.START_DATE -> "Select Start Date"
                    AnalyticsState.DatePickerType.END_DATE -> "Select End Date"
                }
            )
        },
        text = {
            Text("Date picker implementation would go here. For now, using current date.")
        },
        confirmButton = {
            Button(
                onClick = {
                    val instant = kotlinx.datetime.Clock.System.now()
                    val timeZone = kotlinx.datetime.TimeZone.currentSystemDefault()
                    val now = instant.toLocalDateTime(timeZone)
                    onDateSelected(now)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
