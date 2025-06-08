package com.sulavtimsina.expensetracker.settings.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state = viewModel.state
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            SettingsSection(title = "Appearance") {
                SettingsSwitchItem(
                    title = "Dark Theme",
                    subtitle = "Enable dark mode",
                    checked = state.isDarkTheme,
                    onCheckedChange = { viewModel.onAction(SettingsAction.OnThemeToggle) },
                    icon = Icons.Default.Brightness4
                )
            }
            
            // Notifications Section
            SettingsSection(title = "Notifications") {
                SettingsSwitchItem(
                    title = "Enable Notifications",
                    subtitle = "Receive expense reminders",
                    checked = state.notificationsEnabled,
                    onCheckedChange = { viewModel.onAction(SettingsAction.OnNotificationsToggle) },
                    icon = Icons.Default.Notifications
                )
            }
            
            // Currency Section
            SettingsSection(title = "Currency") {
                SettingsClickableItem(
                    title = "Default Currency",
                    subtitle = state.selectedCurrency,
                    onClick = { viewModel.onAction(SettingsAction.OnCurrencyChange) },
                    icon = Icons.Default.MonetizationOn
                )
            }
            
            // Data Section
            SettingsSection(title = "Data Management") {
                SettingsClickableItem(
                    title = "Export Data",
                    subtitle = "Export your expenses to CSV",
                    onClick = { viewModel.onAction(SettingsAction.OnExportData) },
                    icon = Icons.Default.Download
                )
                
                SettingsClickableItem(
                    title = "Clear All Data",
                    subtitle = "Delete all expenses permanently",
                    onClick = { viewModel.onAction(SettingsAction.OnClearData) },
                    icon = Icons.Default.Delete,
                    isDestructive = true
                )
            }
            
            // About Section
            SettingsSection(title = "About") {
                SettingsClickableItem(
                    title = "App Version",
                    subtitle = state.appVersion,
                    onClick = { viewModel.onAction(SettingsAction.OnAbout) },
                    icon = Icons.Default.Info
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingsClickableItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDestructive) {
                    MaterialTheme.colors.error
                } else {
                    MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                },
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = if (isDestructive) {
                        MaterialTheme.colors.error
                    } else {
                        MaterialTheme.colors.onSurface
                    }
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2,
                    color = if (isDestructive) {
                        MaterialTheme.colors.error.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    }
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
