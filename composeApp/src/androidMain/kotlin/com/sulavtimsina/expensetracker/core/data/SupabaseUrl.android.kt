package com.sulavtimsina.expensetracker.core.data

actual fun getSupabaseUrl(): String {
    // Android emulator uses 10.0.2.2 to access host machine
    return "http://10.0.2.2:54321"
}