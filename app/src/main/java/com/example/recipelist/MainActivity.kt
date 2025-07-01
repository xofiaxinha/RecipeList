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
import com.example.recipelist.data.datastore.SettingsDataStore
import com.example.recipelist.data.repository.SettingsRepository
import com.example.recipelist.ui.components.RequestNotificationPermission
import com.example.recipelist.viewmodel.SettingsViewModel
import com.example.recipelist.viewmodel.SettingsViewModelFactory

class MainActivity : ComponentActivity() {
    private val settingsDataStore by lazy { SettingsDataStore(this) }
    private val settingsRepository by lazy { SettingsRepository(settingsDataStore) }

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(settingsRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            RecipeListTheme(darkTheme = isDarkTheme) {
                RequestNotificationPermission()
                MainScreen(settingsViewModel = settingsViewModel)
            }
        }
    }
}