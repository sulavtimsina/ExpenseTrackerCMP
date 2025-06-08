package com.sulavtimsina.expensetracker.di

import com.sulavtimsina.expensetracker.analytics.di.analyticsModule
import com.sulavtimsina.expensetracker.settings.di.settingsModule
import com.sulavtimsina.expensetracker.database.ExpenseDatabase
import org.koin.dsl.module

expect val databaseModule: org.koin.core.module.Module

val coreModule = module {
    includes(databaseModule, expenseModule, analyticsModule, settingsModule)
}

