package com.sulavtimsina.expensetracker.core.data

actual fun getSupabaseUrl(): String {
    // iOS simulator uses localhost to access host machine
    return "http://localhost:54321"
}