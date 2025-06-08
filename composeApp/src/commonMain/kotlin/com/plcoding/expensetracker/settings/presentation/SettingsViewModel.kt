package com.plcoding.expensetracker.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    
    var state by mutableStateOf(SettingsState())
        private set
    
    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnThemeToggle -> {
                state = state.copy(
                    isDarkTheme = !state.isDarkTheme
                )
            }
            is SettingsAction.OnNotificationsToggle -> {
                state = state.copy(
                    notificationsEnabled = !state.notificationsEnabled
                )
            }
            is SettingsAction.OnCurrencyChange -> {
                // This would typically open a selection dialog
                // For now, we'll cycle through currencies
                val currentIndex = state.availableCurrencies.indexOf(state.selectedCurrency)
                val nextIndex = (currentIndex + 1) % state.availableCurrencies.size
                state = state.copy(
                    selectedCurrency = state.availableCurrencies[nextIndex]
                )
            }
            is SettingsAction.OnExportData -> {
                // Export functionality would be implemented here
                // For now, just a placeholder
            }
            is SettingsAction.OnClearData -> {
                // Clear data functionality would be implemented here
                // For now, just a placeholder
            }
            is SettingsAction.OnAbout -> {
                // Show about dialog or navigate to about screen
                // For now, just a placeholder
            }
        }
    }
}
