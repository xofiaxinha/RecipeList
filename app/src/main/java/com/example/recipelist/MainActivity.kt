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
import androidx.lifecycle.ViewModelProvider
import com.example.recipelist.data.repository.AuthRepository
import com.example.recipelist.viewmodel.AuthViewModel
import com.example.recipelist.viewmodel.AuthViewModelFactory
import com.example.recipelist.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel = ViewModelProvider(this, AuthViewModelFactory(AuthRepository())).get(
            AuthViewModel::class.java)
        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
            RecipeListTheme(darkTheme = isDarkTheme) {
                MainScreen(settingsViewModel = settingsViewModel, authViewModel = authViewModel)
            }
        }
    }
}