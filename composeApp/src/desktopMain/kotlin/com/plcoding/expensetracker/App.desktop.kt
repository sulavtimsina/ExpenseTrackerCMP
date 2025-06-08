package com.plcoding.expensetracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.plcoding.expensetracker.analytics.presentation.AnalyticsScreen
import com.plcoding.expensetracker.core.presentation.AppBottomNavigation
import com.plcoding.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseScreen
import com.plcoding.expensetracker.expense.presentation.expense_detail.ExpenseDetailScreen
import com.plcoding.expensetracker.expense.presentation.expense_list.ExpenseListScreen
import com.plcoding.expensetracker.settings.presentation.SettingsScreen
import org.koin.compose.KoinApplication
import com.plcoding.expensetracker.di.coreModule

@Composable
actual fun App() {
    KoinApplication(application = {
        modules(coreModule)
    }) {
        MaterialTheme {
            val navController = rememberNavController()
            val currentRoute by navController.currentBackStackEntryAsState()
            
            Scaffold(
                bottomBar = {
                    AppBottomNavigation(
                        currentRoute = currentRoute?.destination?.route,
                        onNavigateToDestination = { route ->
                            navController.navigate(route) {
                                // Pop up to the start destination to avoid building up a large stack
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "analytics", // Changed default to analytics
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("analytics") {
                        AnalyticsScreen(
                            onNavigateBack = { } // No back action needed for bottom nav destination
                        )
                    }
                    
                    composable("expense_list") {
                        ExpenseListScreen(
                            onNavigateToAddExpense = {
                                navController.navigate("add_expense")
                            },
                            onNavigateToExpenseDetail = { expenseId ->
                                navController.navigate("expense_detail/$expenseId")
                            },
                            onNavigateToAnalytics = {
                                navController.navigate("analytics")
                            }
                        )
                    }
                    
                    composable("settings") {
                        SettingsScreen()
                    }
                    
                    composable("add_expense") {
                        AddEditExpenseScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    
                    composable("edit_expense/{expenseId}") { backStackEntry ->
                        val expenseId = backStackEntry.arguments?.getString("expenseId")
                        AddEditExpenseScreen(
                            expenseId = expenseId,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    
                    composable("expense_detail/{expenseId}") { backStackEntry ->
                        val expenseId = backStackEntry.arguments?.getString("expenseId") ?: return@composable
                        ExpenseDetailScreen(
                            expenseId = expenseId,
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onNavigateToEdit = { id ->
                                navController.navigate("edit_expense/$id")
                            }
                        )
                    }
                }
            }
        }
    }
}
