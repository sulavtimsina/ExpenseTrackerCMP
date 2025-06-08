package com.plcoding.expensetracker.expense.presentation.add_edit_expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.expensetracker.expense.domain.ExpenseCategory
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddEditExpenseScreen(
    expenseId: String? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditExpenseViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(expenseId) {
        expenseId?.let {
            viewModel.onAction(AddEditExpenseAction.LoadExpense(it))
        }
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (expenseId != null) "Edit Expense" else "Add Expense",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Amount Field
                OutlinedTextField(
                    value = state.amount,
                    onValueChange = { viewModel.onAction(AddEditExpenseAction.OnAmountChange(it)) },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = state.amountError != null,
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                
                state.amountError?.let { error ->
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                // Category Selection
                Text(
                    text = "Category",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                CategorySelection(
                    selectedCategory = state.category,
                    onCategorySelected = { 
                        viewModel.onAction(AddEditExpenseAction.OnCategoryChange(it))
                    }
                )

                // Note Field
                OutlinedTextField(
                    value = state.note,
                    onValueChange = { viewModel.onAction(AddEditExpenseAction.OnNoteChange(it)) },
                    label = { Text("Note (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )

                // Date Field (simplified - you can enhance this with date picker)
                OutlinedTextField(
                    value = "${state.date.year}-${state.date.monthNumber.toString().padStart(2, '0')}-${state.date.dayOfMonth.toString().padStart(2, '0')}",
                    onValueChange = { /* Handle date change */ },
                    label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = { viewModel.onAction(AddEditExpenseAction.OnSave) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = if (expenseId != null) "Update Expense" else "Add Expense",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Error handling
    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or toast
            viewModel.onAction(AddEditExpenseAction.OnClearError)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CategorySelection(
    selectedCategory: ExpenseCategory?,
    onCategorySelected: (ExpenseCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = ExpenseCategory.entries
    val chunkedCategories = categories.chunked(3)

    Column(modifier = modifier) {
        chunkedCategories.forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCategories.forEach { category ->
                    Surface(
                        onClick = { onCategorySelected(category) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedCategory == category) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.surface
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (selectedCategory == category) {
                                MaterialTheme.colors.primary
                            } else {
                                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                            }
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = category.displayName,
                                fontSize = 12.sp,
                                color = if (selectedCategory == category) {
                                    MaterialTheme.colors.onPrimary
                                } else {
                                    MaterialTheme.colors.onSurface
                                }
                            )
                        }
                    }
                }
                
                // Fill remaining space if row is not complete
                repeat(3 - rowCategories.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            
            if (rowCategories != chunkedCategories.last()) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
