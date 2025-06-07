package com.plcoding.expensetracker

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plcoding.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseScreen
import com.plcoding.expensetracker.expense.presentation.expense_detail.ExpenseDetailScreen
import com.plcoding.expensetracker.expense.presentation.expense_list.ExpenseListScreen
import org.koin.compose.KoinApplication
import com.plcoding.expensetracker.di.coreModule

@Composable
expect fun App()
