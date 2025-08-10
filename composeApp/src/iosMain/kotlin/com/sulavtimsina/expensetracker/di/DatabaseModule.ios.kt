package com.sulavtimsina.expensetracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.sulavtimsina.expensetracker.database.ExpenseDatabase
import org.koin.dsl.module

actual val databaseModule =
    module {
        single<SqlDriver> {
            NativeSqliteDriver(
                schema = ExpenseDatabase.Schema,
                name = "expense.db",
            )
        }

        single<ExpenseDatabase> {
            ExpenseDatabase(get())
        }
    }
