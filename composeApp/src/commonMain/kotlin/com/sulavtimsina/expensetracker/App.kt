package com.sulavtimsina.expensetracker

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseScreen
import com.sulavtimsina.expensetracker.expense.presentation.expense_detail.ExpenseDetailScreen
import com.sulavtimsina.expensetracker.expense.presentation.expense_list.ExpenseListScreen
import org.koin.compose.KoinApplication
import com.sulavtimsina.expensetracker.di.coreModule

@Composable
expect fun App()
