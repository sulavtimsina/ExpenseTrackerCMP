package com.sulavtimsina.expensetracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sulavtimsina.expensetracker.database.ExpenseDatabase
// Firebase module removed - using Supabase instead
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = ExpenseDatabase.Schema,
            context = androidContext(),
            name = "expense.db"
        )
    }
    
    single<ExpenseDatabase> {
        ExpenseDatabase(get())
    }
    
    // Firebase module removed - using Supabase for all platforms
}
