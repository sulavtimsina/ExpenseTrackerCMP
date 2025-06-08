package com.plcoding.expensetracker.analytics.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.expensetracker.analytics.domain.DailyData
import com.plcoding.expensetracker.analytics.domain.MonthlyData
import kotlin.math.max
import kotlin.math.round

// Multiplatform-compatible number formatting
private fun Double.formatCurrency(): String = 
    "${(round(this * 100) / 100.0)}"

@Composable
fun LineChart(
    monthlyData: List<MonthlyData>,
    dailyData: List<DailyData>,
    showMonthly: Boolean,
    modifier: Modifier = Modifier
) {
    val animationProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (showMonthly) "Monthly Spending Trend" else "Daily Spending Trend",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (showMonthly && monthlyData.isNotEmpty()) {
            Text(
                text = "Last ${monthlyData.size} months",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else if (!showMonthly && dailyData.isNotEmpty()) {
            Text(
                text = "Last ${dailyData.size} days",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
            ) {
                if (showMonthly) {
                    drawMonthlyLineChart(monthlyData, animationProgress)
                } else {
                    drawDailyLineChart(dailyData, animationProgress)
                }
            }
        }

        if (showMonthly && monthlyData.isNotEmpty()) {
            Text(
                text = "Monthly Summary",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            MonthlyDataSummary(monthlyData)
        } else if (!showMonthly && dailyData.isNotEmpty()) {
            Text(
                text = "Recent Days",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            DailyDataSummary(dailyData)
        }
    }
}

@Composable
private fun MonthlyDataSummary(data: List<MonthlyData>) {
    val topSpendingMonths = data.sortedByDescending { it.amount }.take(3)
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        topSpendingMonths.forEach { monthData ->
            SummaryCard(
                title = "${monthData.month} ${monthData.year}",
                value = "$${monthData.amount.formatCurrency()}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DailyDataSummary(data: List<DailyData>) {
    val recentDays = data.takeLast(5)
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        recentDays.forEach { dayData ->
            SummaryCard(
                title = "${dayData.date.dayOfMonth}/${dayData.date.monthNumber}",
                value = "$${dayData.amount.formatCurrency()}",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

private fun DrawScope.drawMonthlyLineChart(data: List<MonthlyData>, progress: Float) {
    if (data.isEmpty()) return
    
    val maxAmount = data.maxOfOrNull { it.amount } ?: 0.0
    if (maxAmount == 0.0) return
    
    val chartWidth = size.width - 32.dp.toPx()
    val chartHeight = size.height - 64.dp.toPx()
    val stepX = chartWidth / (data.size - 1).coerceAtLeast(1)
    
    drawGridLines()
    
    val path = Path()
    data.forEachIndexed { index, monthData ->
        val x = 16.dp.toPx() + index * stepX
        val y = size.height - 32.dp.toPx() - (monthData.amount / maxAmount * chartHeight).toFloat()
        
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
        
        // Draw data points
        drawCircle(
            color = Color(0xFF2196F3),
            radius = 4.dp.toPx(),
            center = Offset(x, y)
        )
    }
    
    // Draw the line with animation
    drawPath(
        path = path,
        color = Color(0xFF2196F3),
        style = Stroke(
            width = 3.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(0f, path.getTotalLength() * (1f - progress))
            )
        )
    )
}

private fun DrawScope.drawDailyLineChart(data: List<DailyData>, progress: Float) {
    if (data.isEmpty()) return
    
    val maxAmount = data.maxOfOrNull { it.amount } ?: 0.0
    if (maxAmount == 0.0) return
    
    val chartWidth = size.width - 32.dp.toPx()
    val chartHeight = size.height - 64.dp.toPx()
    val stepX = chartWidth / (data.size - 1).coerceAtLeast(1)
    
    drawGridLines()
    
    val path = Path()
    data.forEachIndexed { index, dayData ->
        val x = 16.dp.toPx() + index * stepX
        val y = size.height - 32.dp.toPx() - (dayData.amount / maxAmount * chartHeight).toFloat()
        
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
        
        // Draw data points
        drawCircle(
            color = Color(0xFF4CAF50),
            radius = 4.dp.toPx(),
            center = Offset(x, y)
        )
    }
    
    // Draw the line with animation
    drawPath(
        path = path,
        color = Color(0xFF4CAF50),
        style = Stroke(
            width = 3.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(0f, path.getTotalLength() * (1f - progress))
            )
        )
    )
}

private fun DrawScope.drawGridLines() {
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    val stepY = size.height / 5
    
    // Horizontal grid lines
    for (i in 0..5) {
        val y = i * stepY
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.dp.toPx()
        )
    }
    
    // Vertical grid lines
    val stepX = size.width / 4
    for (i in 0..4) {
        val x = i * stepX
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }
}

// Extension function for Path.getTotalLength() - simplified implementation
private fun Path.getTotalLength(): Float {
    // This is a simplified implementation
    // In a real app, you might want to use a proper path measure
    return 1000f
}
