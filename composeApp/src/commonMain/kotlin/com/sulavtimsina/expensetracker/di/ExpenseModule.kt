package com.sulavtimsina.expensetracker.di

import com.sulavtimsina.expensetracker.expense.data.database.ExpenseDatabaseSource
import com.sulavtimsina.expensetracker.expense.data.repository.ExpenseRepositoryImplLocal
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import com.sulavtimsina.expensetracker.expense.presentation.add_edit_expense.AddEditExpenseViewModel
import com.sulavtimsina.expensetracker.expense.presentation.expense_detail.ExpenseDetailViewModel
import com.sulavtimsina.expensetracker.expense.presentation.expense_list.ExpenseListViewModel
import com.sulavtimsina.expensetracker.data.SampleDataProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val expenseModule = module {
    // Data sources
    singleOf(::ExpenseDatabaseSource)
    
    // Repository - Android will override this with Firebase-enabled version
    single<ExpenseRepository> {
        ExpenseRepositoryImplLocal(
            databaseSource = get()
        )
    }
    
    // Other
    singleOf(::SampleDataProvider)
    
    // ViewModels
    viewModelOf(::ExpenseListViewModel)
    viewModelOf(::AddEditExpenseViewModel)
    viewModelOf(::ExpenseDetailViewModel)
}

