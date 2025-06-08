package com.sulavtimsina.expensetracker.auth.presentation.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sulavtimsina.expensetracker.expense.data.cloud.android.AndroidFirebaseSource
import org.koin.compose.koinInject

@Composable
fun AndroidAuthWrapper(
    content: @Composable () -> Unit
) {
    val firebaseSource: AndroidFirebaseSource = koinInject()
    var isAuthenticated by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        if (firebaseSource.getCurrentUserId() == null) {
            firebaseSource.signInAnonymously()
        }
        isAuthenticated = true
    }
    
    if (isAuthenticated) {
        content()
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
