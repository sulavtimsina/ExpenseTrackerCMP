package com.sulavtimsina.expensetracker.settings.presentation

import kotlin.test.*

class SettingsActionTest {

    @Test
    fun `OnThemeToggle is data object`() {
        // Given
        val action1 = SettingsAction.OnThemeToggle
        val action2 = SettingsAction.OnThemeToggle

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2) // Same instance
    }

    @Test
    fun `OnNotificationsToggle is data object`() {
        // Given
        val action1 = SettingsAction.OnNotificationsToggle
        val action2 = SettingsAction.OnNotificationsToggle

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnCurrencyChange is data object`() {
        // Given
        val action1 = SettingsAction.OnCurrencyChange
        val action2 = SettingsAction.OnCurrencyChange

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnExportData is data object`() {
        // Given
        val action1 = SettingsAction.OnExportData
        val action2 = SettingsAction.OnExportData

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnClearData is data object`() {
        // Given
        val action1 = SettingsAction.OnClearData
        val action2 = SettingsAction.OnClearData

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnAbout is data object`() {
        // Given
        val action1 = SettingsAction.OnAbout
        val action2 = SettingsAction.OnAbout

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnSignIn is data object`() {
        // Given
        val action1 = SettingsAction.OnSignIn
        val action2 = SettingsAction.OnSignIn

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnSignOut is data object`() {
        // Given
        val action1 = SettingsAction.OnSignOut
        val action2 = SettingsAction.OnSignOut

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnToggleSync is data object`() {
        // Given
        val action1 = SettingsAction.OnToggleSync
        val action2 = SettingsAction.OnToggleSync

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnManualSync is data object`() {
        // Given
        val action1 = SettingsAction.OnManualSync
        val action2 = SettingsAction.OnManualSync

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `OnClearSyncError is data object`() {
        // Given
        val action1 = SettingsAction.OnClearSyncError
        val action2 = SettingsAction.OnClearSyncError

        // Then
        assertEquals(action1, action2)
        assertTrue(action1 === action2)
    }

    @Test
    fun `all actions implement SettingsAction interface`() {
        // Given
        val actions: List<SettingsAction> = listOf(
            SettingsAction.OnThemeToggle,
            SettingsAction.OnNotificationsToggle,
            SettingsAction.OnCurrencyChange,
            SettingsAction.OnExportData,
            SettingsAction.OnClearData,
            SettingsAction.OnAbout,
            SettingsAction.OnSignIn,
            SettingsAction.OnSignOut,
            SettingsAction.OnToggleSync,
            SettingsAction.OnManualSync,
            SettingsAction.OnClearSyncError
        )

        // Then
        actions.forEach { action ->
            assertTrue(action is SettingsAction)
        }
    }

    @Test
    fun `different actions are not equal`() {
        // Given
        val actions = listOf(
            SettingsAction.OnThemeToggle,
            SettingsAction.OnNotificationsToggle,
            SettingsAction.OnCurrencyChange,
            SettingsAction.OnExportData,
            SettingsAction.OnClearData,
            SettingsAction.OnAbout,
            SettingsAction.OnSignIn,
            SettingsAction.OnSignOut,
            SettingsAction.OnToggleSync,
            SettingsAction.OnManualSync,
            SettingsAction.OnClearSyncError
        )

        // Then
        for (i in actions.indices) {
            for (j in actions.indices) {
                if (i != j) {
                    assertNotEquals(actions[i], actions[j])
                }
            }
        }
    }

    @Test
    fun `actions have meaningful toString representations`() {
        // Given
        val actions = listOf(
            SettingsAction.OnThemeToggle,
            SettingsAction.OnNotificationsToggle,
            SettingsAction.OnCurrencyChange,
            SettingsAction.OnSignIn,
            SettingsAction.OnSignOut
        )

        // Then
        actions.forEach { action ->
            val toString = action.toString()
            assertNotNull(toString)
            assertTrue(toString.isNotEmpty())
            assertTrue(toString.contains("On"))
        }
    }

    @Test
    fun `SettingsAction is sealed interface`() {
        // This test verifies that all known actions are covered
        // and that SettingsAction is properly sealed
        
        val action: SettingsAction = SettingsAction.OnThemeToggle
        
        // Should be able to use when expression exhaustively
        val result = when (action) {
            SettingsAction.OnThemeToggle -> "theme"
            SettingsAction.OnNotificationsToggle -> "notifications"
            SettingsAction.OnCurrencyChange -> "currency"
            SettingsAction.OnExportData -> "export"
            SettingsAction.OnClearData -> "clear"
            SettingsAction.OnAbout -> "about"
            SettingsAction.OnSignIn -> "signin"
            SettingsAction.OnSignOut -> "signout"
            SettingsAction.OnToggleSync -> "sync"
            SettingsAction.OnManualSync -> "manual"
            SettingsAction.OnClearSyncError -> "clear_error"
        }
        
        assertEquals("theme", result)
    }
}