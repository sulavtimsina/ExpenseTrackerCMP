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
import com.sulavtimsina.expensetracker.auth.presentation.AuthScreen
import com.sulavtimsina.expensetracker.auth.presentation.AuthViewModel
import com.sulavtimsina.expensetracker.expense.data.repository.ExpenseRepositoryImplHybrid
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import org.koin.compose.koinInject
import kotlinx.coroutines.launch

@Composable
actual fun App() {
    MaterialTheme {
        val authViewModel = koinInject<AuthViewModel>()
        val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
        val expenseRepository = koinInject<ExpenseRepository>()
        
        // Trigger sync when user becomes authenticated (including app start with existing session)
        LaunchedEffect(isAuthenticated) {
            if (isAuthenticated && expenseRepository is ExpenseRepositoryImplHybrid) {
                val result = expenseRepository.triggerSyncForAuthenticatedUser()
                when (result) {
                    is com.sulavtimsina.expensetracker.core.domain.Result.Success -> {
                        println("Auto-sync started for authenticated user: ${result.data}")
                    }
                    is com.sulavtimsina.expensetracker.core.domain.Result.Error -> {
                        println("Failed to auto-start sync: ${result.error}")
                    }
                }
            }
        }
        
        if (!isAuthenticated) {
            AuthScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Trigger sync for the authenticated user
                    if (expenseRepository is ExpenseRepositoryImplHybrid) {
                        kotlinx.coroutines.GlobalScope.launch {
                            val result = expenseRepository.triggerSyncForAuthenticatedUser()
                            when (result) {
                                is com.sulavtimsina.expensetracker.core.domain.Result.Success -> {
                                    println("Sync started for user: ${result.data}")
                                }
                                is com.sulavtimsina.expensetracker.core.domain.Result.Error -> {
                                    println("Failed to start sync: ${result.error}")
                                }
                            }
                        }
                    }
                }
            )
        } else {
            ExpenseApp()
        }
    }
}

@Composable
private fun ExpenseApp() {
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
            startDestination = "expense_list", // Start with expenses
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