package com.plcoding.expensetracker

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plcoding.expensetracker.analytics.presentation.AnalyticsScreen
import com.plcoding.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseScreen
import com.plcoding.expensetracker.expense.presentation.expense_detail.ExpenseDetailScreen
import com.plcoding.expensetracker.expense.presentation.expense_list.ExpenseListScreen

@Composable
actual fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = "expense_list"
        ) {
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
            
            composable("analytics") {
                AnalyticsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

