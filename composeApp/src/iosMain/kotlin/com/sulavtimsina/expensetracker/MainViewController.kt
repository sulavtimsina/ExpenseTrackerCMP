package com.sulavtimsina.expensetracker

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import com.sulavtimsina.expensetracker.di.coreModule

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS
    if (org.koin.core.context.GlobalContext.getOrNull() == null) {
        startKoin {
            modules(coreModule)
        }
    }
    App()
}