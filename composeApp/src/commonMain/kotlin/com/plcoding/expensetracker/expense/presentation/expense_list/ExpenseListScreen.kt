package com.plcoding.expensetracker.expense.presentation.expense_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.expensetracker.expense.domain.Expense
import com.plcoding.expensetracker.expense.presentation.components.CategoryFilterChips
import com.plcoding.expensetracker.expense.presentation.components.ExpenseCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExpenseListScreen(
    onNavigateToAddExpense: () -> Unit,
    onNavigateToExpenseDetail: (String) -> Unit,
    onNavigateToAnalytics: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExpenseListViewModel = koinViewModel()
) {
    val expenses by viewModel.expenses.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            // Handle error display
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Expense Tracker",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                actions = {
                    IconButton(
                        onClick = onNavigateToAnalytics
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Analytics",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddExpense,
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Expense"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onAction(ExpenseListAction.OnSearchQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search expenses...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Category Filter
            CategoryFilterChips(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    viewModel.onAction(ExpenseListAction.OnCategoryFilter(category))
                },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Expense List
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                expenses.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "No expenses found",
                                fontSize = 18.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                "Tap + to add your first expense",
                                fontSize = 14.sp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = expenses,
                            key = { it.id }
                        ) { expense ->
                            ExpenseCard(
                                expense = expense,
                                onExpenseClick = { onNavigateToExpenseDetail(it.id) },
                                onDeleteExpense = { 
                                    viewModel.onAction(ExpenseListAction.OnDeleteExpense(it))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Error handling
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or toast
            viewModel.onAction(ExpenseListAction.OnClearError)
        }
    }
}

