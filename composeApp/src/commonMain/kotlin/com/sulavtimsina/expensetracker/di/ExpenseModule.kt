package com.sulavtimsina.expensetracker.di

import com.sulavtimsina.expensetracker.data.SampleDataProvider
import com.sulavtimsina.expensetracker.expense.data.cloud.ExpenseCloudSource
import com.sulavtimsina.expensetracker.expense.data.cloud.SupabaseExpenseSource
import com.sulavtimsina.expensetracker.expense.data.database.ExpenseDatabaseSource
import com.sulavtimsina.expensetracker.expense.data.repository.ExpenseRepositoryImplHybrid
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseViewModel
import com.sulavtimsina.expensetracker.expense.presentation.expense_detail.ExpenseDetailViewModel
import com.sulavtimsina.expensetracker.expense.presentation.expense_list.ExpenseListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val expenseModule =
    module {
        // Data sources
        singleOf(::ExpenseDatabaseSource)
        single<ExpenseCloudSource> { SupabaseExpenseSource() }

        // Repository - Hybrid version with local + cloud sync
        single<ExpenseRepository> {
            ExpenseRepositoryImplHybrid(
                localSource = get(),
                cloudSource = get(),
            )
        }

        // Other
        singleOf(::SampleDataProvider)

        // ViewModels
        viewModelOf(::ExpenseListViewModel)
        viewModelOf(::AddEditExpenseViewModel)
        viewModelOf(::ExpenseDetailViewModel)
    }
