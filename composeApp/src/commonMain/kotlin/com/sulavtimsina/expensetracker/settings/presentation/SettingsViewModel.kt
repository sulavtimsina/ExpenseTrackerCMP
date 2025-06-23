package com.sulavtimsina.expensetracker.settings.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sulavtimsina.expensetracker.core.domain.Result
import com.sulavtimsina.expensetracker.expense.data.repository.ExpenseRepositoryImplHybrid
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class SettingsViewModel(
    private val expenseRepository: ExpenseRepositoryImplHybrid
) : ViewModel() {
    
    var state by mutableStateOf(SettingsState())
        private set
    
    init {
        checkSignInStatus()
    }
    
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
            is SettingsAction.OnSignIn -> {
                signInToSupabase()
            }
            is SettingsAction.OnSignOut -> {
                signOutFromSupabase()
            }
            is SettingsAction.OnToggleSync -> {
                toggleSync()
            }
            is SettingsAction.OnManualSync -> {
                performManualSync()
            }
            is SettingsAction.OnClearSyncError -> {
                state = state.copy(syncError = null)
            }
        }
    }
    
    private fun checkSignInStatus() {
        // Check if user is already signed in
        // This would be implemented based on Supabase auth state
        // For now, we'll assume user is not signed in initially
    }
    
    private fun signInToSupabase() {
        viewModelScope.launch {
            state = state.copy(syncInProgress = true, syncError = null)
            
            val result = expenseRepository.signInAndStartSync()
            
            when (result) {
                is Result.Success -> {
                    state = state.copy(
                        isSignedIn = true,
                        userId = result.data,
                        isSyncEnabled = true,
                        syncInProgress = false,
                        lastSyncTime = Clock.System.now().toString()
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        syncInProgress = false,
                        syncError = result.error.name
                    )
                }
            }
        }
    }
    
    private fun signOutFromSupabase() {
        viewModelScope.launch {
            // Sign out logic would be implemented here
            state = state.copy(
                isSignedIn = false,
                userId = null,
                isSyncEnabled = false,
                lastSyncTime = null,
                syncError = null
            )
        }
    }
    
    private fun toggleSync() {
        if (state.isSignedIn) {
            state = state.copy(
                isSyncEnabled = !state.isSyncEnabled
            )
        } else {
            // Need to sign in first
            signInToSupabase()
        }
    }
    
    private fun performManualSync() {
        if (!state.isSignedIn) {
            state = state.copy(syncError = "Please sign in first")
            return
        }
        
        viewModelScope.launch {
            state = state.copy(syncInProgress = true, syncError = null)
            
            try {
                // Manual sync would trigger a full sync
                // For now, just update the last sync time
                state = state.copy(
                    syncInProgress = false,
                    lastSyncTime = Clock.System.now().toString()
                )
            } catch (e: Exception) {
                state = state.copy(
                    syncInProgress = false,
                    syncError = e.message ?: "Sync failed"
                )
            }
        }
    }
}
