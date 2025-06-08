package com.sulavtimsina.expensetracker.settings.presentation

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val selectedCurrency: String = "USD",
    val availableCurrencies: List<String> = listOf("USD", "EUR", "GBP", "JPY", "CAD"),
    val appVersion: String = "1.0.0"
)
