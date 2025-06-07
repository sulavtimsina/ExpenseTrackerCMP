package com.plcoding.expensetracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.plcoding.expensetracker.database.ExpenseDatabase
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
}
