package com.sulavtimsina.expensetracker.di

import com.sulavtimsina.expensetracker.analytics.di.analyticsModule
import com.sulavtimsina.expensetracker.auth.di.authModule
import com.sulavtimsina.expensetracker.settings.di.settingsModule
import org.koin.dsl.module

expect val databaseModule: org.koin.core.module.Module

val coreModule =
    module {
        includes(databaseModule, expenseModule, analyticsModule, settingsModule, authModule)
    }
