package com.sulavtimsina.expensetracker.settings.presentation

import kotlin.test.*

class SettingsStateTest {

    @Test
    fun `SettingsState has correct default values`() {
        // When
        val state = SettingsState()

        // Then
        assertFalse(state.isDarkTheme)
        assertTrue(state.notificationsEnabled)
        assertEquals("USD", state.selectedCurrency)
        assertEquals(listOf("USD", "EUR", "GBP", "JPY", "CAD"), state.availableCurrencies)
        assertEquals("1.0.0", state.appVersion)
        assertFalse(state.isSyncEnabled)
        assertFalse(state.isSignedIn)
        assertNull(state.userId)
        assertNull(state.lastSyncTime)
        assertFalse(state.syncInProgress)
        assertNull(state.syncError)
    }

    @Test
    fun `SettingsState can be created with custom values`() {
        // When
        val state = SettingsState(
            isDarkTheme = true,
            notificationsEnabled = false,
            selectedCurrency = "EUR",
            availableCurrencies = listOf("EUR", "USD"),
            appVersion = "2.0.0",
            isSyncEnabled = true,
            isSignedIn = true,
            userId = "user123",
            lastSyncTime = "2024-01-15T12:00:00",
            syncInProgress = true,
            syncError = "Test error"
        )

        // Then
        assertTrue(state.isDarkTheme)
        assertFalse(state.notificationsEnabled)
        assertEquals("EUR", state.selectedCurrency)
        assertEquals(listOf("EUR", "USD"), state.availableCurrencies)
        assertEquals("2.0.0", state.appVersion)
        assertTrue(state.isSyncEnabled)
        assertTrue(state.isSignedIn)
        assertEquals("user123", state.userId)
        assertEquals("2024-01-15T12:00:00", state.lastSyncTime)
        assertTrue(state.syncInProgress)
        assertEquals("Test error", state.syncError)
    }

    @Test
    fun `SettingsState copy works correctly`() {
        // Given
        val originalState = SettingsState()

        // When
        val copiedState = originalState.copy(
            isDarkTheme = true,
            selectedCurrency = "EUR",
            isSignedIn = true
        )

        // Then
        assertTrue(copiedState.isDarkTheme)
        assertEquals("EUR", copiedState.selectedCurrency)
        assertTrue(copiedState.isSignedIn)
        
        // Other values should remain the same as defaults
        assertTrue(copiedState.notificationsEnabled)
        assertEquals("1.0.0", copiedState.appVersion)
        assertFalse(copiedState.isSyncEnabled)
    }

    @Test
    fun `SettingsState equality works correctly`() {
        // Given
        val state1 = SettingsState()
        val state2 = SettingsState()
        val state3 = SettingsState(isDarkTheme = true)

        // Then
        assertEquals(state1, state2)
        assertNotEquals(state1, state3)
        assertNotEquals(state2, state3)
    }

    @Test
    fun `SettingsState with all sync properties set`() {
        // When
        val state = SettingsState(
            isSyncEnabled = true,
            isSignedIn = true,
            userId = "test-user-id",
            lastSyncTime = "2024-01-15T10:30:00",
            syncInProgress = false,
            syncError = null
        )

        // Then
        assertTrue(state.isSyncEnabled)
        assertTrue(state.isSignedIn)
        assertEquals("test-user-id", state.userId)
        assertEquals("2024-01-15T10:30:00", state.lastSyncTime)
        assertFalse(state.syncInProgress)
        assertNull(state.syncError)
    }

    @Test
    fun `SettingsState with sync error`() {
        // When
        val state = SettingsState(
            syncError = "Network connection failed"
        )

        // Then
        assertEquals("Network connection failed", state.syncError)
        assertFalse(state.isSignedIn)
        assertFalse(state.isSyncEnabled)
    }

    @Test
    fun `SettingsState supports all available currencies`() {
        // Given
        val state = SettingsState()
        val expectedCurrencies = listOf("USD", "EUR", "GBP", "JPY", "CAD")

        // Then
        assertEquals(expectedCurrencies, state.availableCurrencies)
        assertEquals(5, state.availableCurrencies.size)
        assertTrue(state.availableCurrencies.contains("USD"))
        assertTrue(state.availableCurrencies.contains("EUR"))
        assertTrue(state.availableCurrencies.contains("GBP"))
        assertTrue(state.availableCurrencies.contains("JPY"))
        assertTrue(state.availableCurrencies.contains("CAD"))
    }

    @Test
    fun `SettingsState default currency is included in available currencies`() {
        // Given
        val state = SettingsState()

        // Then
        assertTrue(state.availableCurrencies.contains(state.selectedCurrency))
    }
}