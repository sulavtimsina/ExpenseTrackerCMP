package com.sulavtimsina.expensetracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.sulavtimsina.expensetracker.analytics.presentation.AnalyticsScreen
import com.sulavtimsina.expensetracker.core.presentation.AppBottomNavigation
import com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseScreen
import com.sulavtimsina.expensetracker.expense.presentation.expense_detail.ExpenseDetailScreen
import com.sulavtimsina.expensetracker.expense.presentation.expense_list.ExpenseListScreen
import com.sulavtimsina.expensetracker.settings.presentation.SettingsScreen
import org.koin.compose.KoinApplication
import com.sulavtimsina.expensetracker.di.coreModule
import com.sulavtimsina.expensetracker.data.SampleDataProvider
import org.koin.compose.koinInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun App() {
    KoinApplication(application = {
        modules(coreModule)
    }) {
        val sampleDataProvider: SampleDataProvider = koinInject()
        
        // Initialize sample data
        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                try {
                    sampleDataProvider.insertSampleData()
                } catch (e: Exception) {
                    // Handle initialization error silently
                }
            }
        }
        
        MaterialTheme {
            // Auth handled by Supabase in common code
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
            // }  // End of AuthWrapper - temporarily disabled
        }
    }
}
