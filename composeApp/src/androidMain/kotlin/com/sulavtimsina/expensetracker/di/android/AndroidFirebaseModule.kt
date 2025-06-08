package com.sulavtimsina.expensetracker.di.android

import com.sulavtimsina.expensetracker.expense.data.cloud.android.AndroidFirebaseSource
import com.sulavtimsina.expensetracker.expense.data.repository.android.AndroidExpenseRepositoryImpl
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val androidFirebaseModule = module {
    singleOf(::AndroidFirebaseSource)
    singleOf(::AndroidExpenseRepositoryImpl) bind ExpenseRepository::class
}
