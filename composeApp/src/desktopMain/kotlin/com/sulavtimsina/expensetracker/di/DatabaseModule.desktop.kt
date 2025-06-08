package com.sulavtimsina.expensetracker.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.sulavtimsina.expensetracker.database.ExpenseDatabase
import org.koin.dsl.module
import java.io.File

actual val databaseModule = module {
    single<SqlDriver> {
        val databasePath = File(System.getProperty("java.io.tmpdir"), "expense.db")
        val driver = JdbcSqliteDriver("jdbc:sqlite:${databasePath.absolutePath}")
        try {
            ExpenseDatabase.Schema.create(driver)
        } catch (e: Exception) {
            // Database might already exist, ignore
        }
        driver
    }
    
    single<ExpenseDatabase> {
        ExpenseDatabase(get())
    }
}
