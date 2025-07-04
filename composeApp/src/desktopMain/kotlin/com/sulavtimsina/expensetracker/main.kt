package com.sulavtimsina.expensetracker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Expense Tracker",
    ) {
        App()
    }
}