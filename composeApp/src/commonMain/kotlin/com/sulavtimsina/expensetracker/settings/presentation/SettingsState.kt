package com.sulavtimsina.expensetracker.settings.presentation

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val selectedCurrency: String = "USD",
    val availableCurrencies: List<String> = listOf("USD", "EUR", "GBP", "JPY", "CAD"),
    val appVersion: String = "1.0.0",
    
    // Supabase sync settings
    val isSyncEnabled: Boolean = false,
    val isSignedIn: Boolean = false,
    val userId: String? = null,
    val lastSyncTime: String? = null,
    val syncInProgress: Boolean = false,
    val syncError: String? = null
)
