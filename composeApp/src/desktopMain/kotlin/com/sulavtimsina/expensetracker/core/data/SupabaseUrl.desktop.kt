package com.sulavtimsina.expensetracker.core.data

actual fun getSupabaseUrl(): String {
    // Desktop uses localhost to access local Supabase
    return "http://localhost:54321"
}