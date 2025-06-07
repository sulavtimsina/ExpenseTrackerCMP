package com.plcoding.expensetracker.di

import com.plcoding.expensetracker.analytics.di.analyticsModule
import com.plcoding.expensetracker.database.ExpenseDatabase
import org.koin.dsl.module

expect val databaseModule: org.koin.core.module.Module

val coreModule = module {
    includes(databaseModule, expenseModule, analyticsModule)
}

