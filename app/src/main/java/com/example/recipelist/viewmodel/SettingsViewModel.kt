
package com.example.recipelist.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SettingsViewModel : ViewModel() {


    val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme


    val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }


    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
    }


    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
    }

    fun setNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun resetPreferences() {
        _isDarkTheme.value = false
        _notificationsEnabled.value = true
    }
}
