package com.example.recipelist


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.recipelist.ui.screens.MainScreen
import com.example.recipelist.ui.theme.RecipeListTheme
import androidx. compose. runtime.getValue
import com.example.recipelist.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            RecipeListTheme(darkTheme = isDarkTheme) {
                MainScreen(settingsViewModel = settingsViewModel)
            }
        }
    }
}
