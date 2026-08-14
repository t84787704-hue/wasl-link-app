package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.WaslApp
import com.example.ui.WaslViewModel
import com.example.ui.theme.WaslTheme

class MainActivity : ComponentActivity() {
    private val viewModel: WaslViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            WaslTheme(
                darkTheme = uiState.isDarkMode,
                dynamicColor = uiState.useDynamicColor
            ) {
                WaslApp(viewModel = viewModel)
            }
        }
    }
}


