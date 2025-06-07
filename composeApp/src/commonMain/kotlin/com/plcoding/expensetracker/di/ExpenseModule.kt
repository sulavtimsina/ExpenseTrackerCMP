package com.plcoding.expensetracker.di

import com.plcoding.expensetracker.expense.data.database.ExpenseDatabaseSource
import com.plcoding.expensetracker.expense.data.repository.ExpenseRepositoryImpl
import com.plcoding.expensetracker.expense.domain.ExpenseRepository
import com.plcoding.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseViewModel
import com.plcoding.expensetracker.expense.presentation.expense_detail.ExpenseDetailViewModel
import com.plcoding.expensetracker.expense.presentation.expense_list.ExpenseListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val expenseModule = module {
    singleOf(::ExpenseDatabaseSource)
    singleOf(::ExpenseRepositoryImpl) bind ExpenseRepository::class
    
    viewModelOf(::ExpenseListViewModel)
    viewModelOf(::AddEditExpenseViewModel)
    viewModelOf(::ExpenseDetailViewModel)
}
