package com.sulavtimsina.expensetracker.core.presentation

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavDestination(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    EXPENSES("expense_list", Icons.Default.AccountBalanceWallet, "Expenses"),
    ANALYTICS("analytics", Icons.Default.Assessment, "Analytics"),
    SETTINGS("settings", Icons.Default.Settings, "Settings")
}

@Composable
fun AppBottomNavigation(
    currentRoute: String?,
    onNavigateToDestination: (String) -> Unit
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary
    ) {
        BottomNavDestination.values().forEach { destination ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = { Text(destination.label) },
                selected = currentRoute == destination.route,
                onClick = { onNavigateToDestination(destination.route) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
