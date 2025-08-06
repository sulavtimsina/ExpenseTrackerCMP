package com.sulavtimsina.expensetracker

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import com.sulavtimsina.expensetracker.di.coreModule

private var koinStarted = false

fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS (only once)
    if (!koinStarted) {
        startKoin {
            modules(coreModule)
        }
        koinStarted = true
    }
    App()
}