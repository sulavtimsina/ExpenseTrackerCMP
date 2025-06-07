package com.plcoding.expensetracker.analytics.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.expensetracker.analytics.domain.DailyData
import com.plcoding.expensetracker.analytics.domain.MonthlyData
import kotlin.math.max

package com.plcoding.expensetracker.analytics.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.plcoding.expensetracker.analytics.domain.DailyData
import com.plcoding.expensetracker.analytics.domain.MonthlyData
import kotlin.math.max

@Composable
fun LineChart(
    monthlyData: List<MonthlyData>,
    dailyData: List<DailyData>,
    showMonthly: Boolean = true,
    modifier: Modifier = Modifier
) {
    var animationProgress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(durationMillis = 1500),
        label = "line_chart_animation"
    )

    LaunchedEffect(monthlyData, dailyData) {
        animationProgress = 1f
    }

    Column(modifier = modifier) {
        // Chart Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (showMonthly) "Monthly Trends" else "Daily Trends",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            if (showMonthly) {
                val totalAmount = monthlyData.sumOf { it.amount }
                Text(
                    text = "Total: $${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chart
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (showMonthly && monthlyData.isNotEmpty()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawMonthlyLineChart(monthlyData, animatedProgress)
                    }
                } else if (!showMonthly && dailyData.isNotEmpty()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawDailyLineChart(dailyData, animatedProgress)
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No data available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Data Summary
        if (showMonthly && monthlyData.isNotEmpty()) {
            MonthlyDataSummary(monthlyData)
        } else if (!showMonthly && dailyData.isNotEmpty()) {
            DailyDataSummary(dailyData)
        }
    }
}

@Composable
private fun MonthlyDataSummary(data: List<MonthlyData>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        data.forEach { monthData ->
            Card(
                modifier = Modifier.width(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${monthData.month} ${monthData.year}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$${String.format("%.0f", monthData.amount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${monthData.transactionCount} transactions",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyDataSummary(data: List<DailyData>) {
    val totalAmount = data.sumOf { it.amount }
    val averagePerDay = totalAmount / data.size
    val maxDay = data.maxByOrNull { it.amount }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SummaryCard(
            title = "Total",
            value = "$${String.format("%.2f", totalAmount)}",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SummaryCard(
            title = "Avg/Day",
            value = "$${String.format("%.2f", averagePerDay)}",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SummaryCard(
            title = "Highest",
            value = "$${String.format("%.2f", maxDay?.amount ?: 0.0)}",
            modifier = Modifier.weight(1f)
        )
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun DrawScope.drawMonthlyLineChart(data: List<MonthlyData>, progress: Float) {
    if (data.isEmpty()) return

    val maxAmount = data.maxOf { it.amount }
    val minAmount = data.minOf { it.amount }
    val range = max(maxAmount - minAmount, 1.0)

    val stepX = size.width / (data.size - 1).coerceAtLeast(1)
    val stepY = size.height * 0.8f

    val points = data.mapIndexed { index, monthData ->
        val x = index * stepX
        val y = size.height - (((monthData.amount - minAmount) / range) * stepY).toFloat() - size.height * 0.1f
        Offset(x, y)
    }

    // Draw grid lines
    drawGridLines()

    // Draw line
    if (points.size > 1) {
        val path = Path()
        val progressedPoints = points.take((points.size * progress).toInt().coerceAtLeast(1))
        
        path.moveTo(progressedPoints.first().x, progressedPoints.first().y)
        for (i in 1 until progressedPoints.size) {
            path.lineTo(progressedPoints[i].x, progressedPoints[i].y)
        }

        drawPath(
            path = path,
            color = Color(0xFF2196F3),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw points
        progressedPoints.forEach { point ->
            drawCircle(
                color = Color(0xFF2196F3),
                radius = 6.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = point
            )
        }
    }
}

private fun DrawScope.drawDailyLineChart(data: List<DailyData>, progress: Float) {
    if (data.isEmpty()) return

    val maxAmount = data.maxOf { it.amount }
    val minAmount = data.minOf { it.amount }
    val range = max(maxAmount - minAmount, 1.0)

    val stepX = size.width / (data.size - 1).coerceAtLeast(1)
    val stepY = size.height * 0.8f

    val points = data.mapIndexed { index, dailyData ->
        val x = index * stepX
        val y = size.height - (((dailyData.amount - minAmount) / range) * stepY).toFloat() - size.height * 0.1f
        Offset(x, y)
    }

    // Draw grid lines
    drawGridLines()

    // Draw line
    if (points.size > 1) {
        val path = Path()
        val progressedPoints = points.take((points.size * progress).toInt().coerceAtLeast(1))
        
        path.moveTo(progressedPoints.first().x, progressedPoints.first().y)
        for (i in 1 until progressedPoints.size) {
            path.lineTo(progressedPoints[i].x, progressedPoints[i].y)
        }

        drawPath(
            path = path,
            color = Color(0xFF4CAF50),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw area under curve
        val fillPath = Path()
        fillPath.addPath(path)
        fillPath.lineTo(progressedPoints.last().x, size.height)
        fillPath.lineTo(progressedPoints.first().x, size.height)
        fillPath.close()

        drawPath(
            path = fillPath,
            color = Color(0xFF4CAF50).copy(alpha = 0.2f)
        )
    }
}

private fun DrawScope.drawGridLines() {
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    val strokeWidth = 1.dp.toPx()

    // Horizontal grid lines
    for (i in 0..4) {
        val y = size.height * i / 4
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
        )
    }
}


@Composable
private fun MonthlyDataSummary(data: List<MonthlyData>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(data.size) { index ->
            val monthData = data[index]
            Card(
                modifier = Modifier.width(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${monthData.month} ${monthData.year}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$${String.format("%.0f", monthData.amount)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${monthData.transactionCount} transactions",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyDataSummary(data: List<DailyData>) {
    val totalAmount = data.sumOf { it.amount }
    val averagePerDay = totalAmount / data.size
    val maxDay = data.maxByOrNull { it.amount }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SummaryCard(
            title = "Total",
            value = "$${String.format("%.2f", totalAmount)}",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SummaryCard(
            title = "Avg/Day",
            value = "$${String.format("%.2f", averagePerDay)}",
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SummaryCard(
            title = "Highest",
            value = "$${String.format("%.2f", maxDay?.amount ?: 0.0)}",
            modifier = Modifier.weight(1f)
        )
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun DrawScope.drawMonthlyLineChart(data: List<MonthlyData>, progress: Float) {
    if (data.isEmpty()) return

    val maxAmount = data.maxOf { it.amount }
    val minAmount = data.minOf { it.amount }
    val range = max(maxAmount - minAmount, 1.0)

    val stepX = size.width / (data.size - 1).coerceAtLeast(1)
    val stepY = size.height * 0.8f

    val points = data.mapIndexed { index, monthData ->
        val x = index * stepX
        val y = size.height - (((monthData.amount - minAmount) / range) * stepY).toFloat() - size.height * 0.1f
        Offset(x, y)
    }

    // Draw grid lines
    drawGridLines()

    // Draw line
    if (points.size > 1) {
        val path = Path()
        val progressedPoints = points.take((points.size * progress).toInt().coerceAtLeast(1))
        
        path.moveTo(progressedPoints.first().x, progressedPoints.first().y)
        for (i in 1 until progressedPoints.size) {
            path.lineTo(progressedPoints[i].x, progressedPoints[i].y)
        }

        drawPath(
            path = path,
            color = Color(0xFF2196F3),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw points
        progressedPoints.forEach { point ->
            drawCircle(
                color = Color(0xFF2196F3),
                radius = 6.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = point
            )
        }
    }
}

private fun DrawScope.drawDailyLineChart(data: List<DailyData>, progress: Float) {
    if (data.isEmpty()) return

    val maxAmount = data.maxOf { it.amount }
    val minAmount = data.minOf { it.amount }
    val range = max(maxAmount - minAmount, 1.0)

    val stepX = size.width / (data.size - 1).coerceAtLeast(1)
    val stepY = size.height * 0.8f

    val points = data.mapIndexed { index, dailyData ->
        val x = index * stepX
        val y = size.height - (((dailyData.amount - minAmount) / range) * stepY).toFloat() - size.height * 0.1f
        Offset(x, y)
    }

    // Draw grid lines
    drawGridLines()

    // Draw line
    if (points.size > 1) {
        val path = Path()
        val progressedPoints = points.take((points.size * progress).toInt().coerceAtLeast(1))
        
        path.moveTo(progressedPoints.first().x, progressedPoints.first().y)
        for (i in 1 until progressedPoints.size) {
            path.lineTo(progressedPoints[i].x, progressedPoints[i].y)
        }

        drawPath(
            path = path,
            color = Color(0xFF4CAF50),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw area under curve
        val fillPath = Path()
        fillPath.addPath(path)
        fillPath.lineTo(progressedPoints.last().x, size.height)
        fillPath.lineTo(progressedPoints.first().x, size.height)
        fillPath.close()

        drawPath(
            path = fillPath,
            color = Color(0xFF4CAF50).copy(alpha = 0.2f)
        )
    }
}

private fun DrawScope.drawGridLines() {
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    val strokeWidth = 1.dp.toPx()

    // Horizontal grid lines
    for (i in 0..4) {
        val y = size.height * i / 4
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
        )
    }
}
