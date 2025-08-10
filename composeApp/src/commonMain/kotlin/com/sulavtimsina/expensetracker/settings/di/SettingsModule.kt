package com.sulavtimsina.expensetracker.settings.di

import com.sulavtimsina.expensetracker.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule =
    module {
        viewModelOf(::SettingsViewModel)
    }
