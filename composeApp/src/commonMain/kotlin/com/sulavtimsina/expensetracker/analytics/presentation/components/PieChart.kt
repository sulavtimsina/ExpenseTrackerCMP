package com.sulavtimsina.expensetracker.analytics.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
// Removed lazy imports - using regular Column now
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sulavtimsina.expensetracker.analytics.domain.CategoryData
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlin.math.round

// Multiplatform-compatible number formatting
private fun Double.formatCurrency(): String = "${(round(this * 100) / 100.0)}"

private fun Double.formatPercentage(): String = "${(round(this * 10) / 10.0)}"

private fun Float.formatPercentage(): String = "${(round(this * 10) / 10.0)}"

@Composable
fun PieChart(
    data: List<CategoryData>,
    modifier: Modifier = Modifier,
) {
    var startAngle by remember { mutableFloatStateOf(0f) }
    val animatedStartAngle by animateFloatAsState(
        targetValue = startAngle,
        animationSpec = tween(durationMillis = 1000),
        label = "pie_chart_animation",
    )

    LaunchedEffect(data) {
        startAngle = 360f
    }

    Column(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
            ) {
                drawPieChart(data, animatedStartAngle)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            data.forEach { categoryData ->
                PieChartLegendItem(
                    categoryData = categoryData,
                    color = getCategoryColor(categoryData.category),
                )
            }
        }
    }
}

@Composable
private fun PieChartLegendItem(
    categoryData: CategoryData,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(color),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = categoryData.category.displayName,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "${categoryData.percentage.formatPercentage()}% • $${categoryData.amount.formatCurrency()}",
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface,
            )
        }

        Text(
            text = "${categoryData.transactionCount} transactions",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface,
        )
    }
}

private fun DrawScope.drawPieChart(
    data: List<CategoryData>,
    animationProgress: Float,
) {
    val total = data.sumOf { it.amount }
    if (total <= 0) return

    val strokeWidth = 20.dp.toPx()
    val radius = (size.minDimension - strokeWidth) / 2
    val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)

    var currentAngle = -90f // Start from top

    data.forEach { categoryData ->
        val sweepAngle = (categoryData.amount / total * 360f * animationProgress / 360f).toFloat()
        val color = getCategoryColor(categoryData.category)

        drawArc(
            color = color,
            startAngle = currentAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            topLeft = androidx.compose.ui.geometry.Offset(center.x - radius, center.y - radius),
            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
        )

        currentAngle += sweepAngle
    }
}

private fun getCategoryColor(category: ExpenseCategory): Color {
    return when (category) {
        ExpenseCategory.FOOD -> Color(0xFF4CAF50)
        ExpenseCategory.TRANSPORTATION -> Color(0xFF2196F3)
        ExpenseCategory.ENTERTAINMENT -> Color(0xFF9C27B0)
        ExpenseCategory.SHOPPING -> Color(0xFFFF9800)
        ExpenseCategory.BILLS -> Color(0xFFF44336)
        ExpenseCategory.HEALTHCARE -> Color(0xFFE91E63)
        ExpenseCategory.EDUCATION -> Color(0xFF3F51B5)
        ExpenseCategory.TRAVEL -> Color(0xFF00BCD4)
        ExpenseCategory.OTHER -> Color(0xFF607D8B)
    }
}
