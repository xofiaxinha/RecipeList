
package com.example.recipelist.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipelist.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {


    val isDarkTheme: StateFlow<Boolean> = repository.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val notificationsEnabled: StateFlow<Boolean> = repository.areNotificationsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true // Valor inicial
        )


    fun toggleTheme() {
        viewModelScope.launch {
            repository.setDarkTheme(!isDarkTheme.value)
        }
    }

    fun toggleNotifications() {
        viewModelScope.launch {
            repository.setNotificationsEnabled(!notificationsEnabled.value)
        }
    }
}


class SettingsViewModelFactory(private val repository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
