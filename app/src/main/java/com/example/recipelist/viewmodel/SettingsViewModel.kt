
package com.example.recipelist.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar


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
    fun updateThemeBasedOnTime() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val shouldUseDarkTheme = currentHour >= 18 || currentHour < 6
        _isDarkTheme.value = shouldUseDarkTheme
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
