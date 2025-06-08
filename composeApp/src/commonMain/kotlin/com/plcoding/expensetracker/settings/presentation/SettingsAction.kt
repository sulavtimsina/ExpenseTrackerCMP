package com.plcoding.expensetracker.settings.presentation

sealed interface SettingsAction {
    data object OnThemeToggle : SettingsAction
    data object OnNotificationsToggle : SettingsAction
    data object OnCurrencyChange : SettingsAction
    data object OnExportData : SettingsAction
    data object OnClearData : SettingsAction
    data object OnAbout : SettingsAction
}
