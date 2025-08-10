package com.sulavtimsina.expensetracker.expense.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryFilterChips(
    selectedCategory: ExpenseCategory?,
    onCategorySelected: (ExpenseCategory?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // "All" chip
        item {
            FilterChip(
                onClick = { onCategorySelected(null) },
                selected = selectedCategory == null,
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "All",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }

        // Category chips
        items(ExpenseCategory.entries) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                selected = selectedCategory == category,
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = category.displayName,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterChip(
    onClick: () -> Unit,
    selected: Boolean,
    shape: androidx.compose.ui.graphics.Shape,
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = shape,
        color =
            if (selected) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.surface
            },
        contentColor =
            if (selected) {
                MaterialTheme.colors.onPrimary
            } else {
                MaterialTheme.colors.onSurface
            },
        border =
            if (!selected) {
                androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                )
            } else {
                null
            },
        modifier = Modifier.height(32.dp),
    ) {
        content()
    }
}
