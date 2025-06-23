package com.sulavtimsina.expensetracker.settings.presentation

sealed interface SettingsAction {
    data object OnThemeToggle : SettingsAction
    data object OnNotificationsToggle : SettingsAction
    data object OnCurrencyChange : SettingsAction
    data object OnExportData : SettingsAction
    data object OnClearData : SettingsAction
    data object OnAbout : SettingsAction
    
    // Supabase sync actions
    data object OnSignIn : SettingsAction
    data object OnSignOut : SettingsAction
    data object OnToggleSync : SettingsAction
    data object OnManualSync : SettingsAction
    data object OnClearSyncError : SettingsAction
}

