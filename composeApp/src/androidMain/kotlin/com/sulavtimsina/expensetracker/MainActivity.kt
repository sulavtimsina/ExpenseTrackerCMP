package com.sulavtimsina.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.sulavtimsina.expensetracker.di.coreModule
import com.sulavtimsina.expensetracker.data.SampleDataProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    
    private val sampleDataProvider: SampleDataProvider by inject()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Koin with Android context
        startKoin {
            androidContext(this@MainActivity.applicationContext)
            modules(coreModule)
        }

        // Initialize sample data in background
        CoroutineScope(Dispatchers.IO).launch {
            try {
                sampleDataProvider.insertSampleData()
            } catch (e: Exception) {
                // Handle initialization error silently
            }
        }

        setContent {
            App()
        }
    }
}



@Preview
@Composable
fun AppAndroidPreview() {
    App()
}