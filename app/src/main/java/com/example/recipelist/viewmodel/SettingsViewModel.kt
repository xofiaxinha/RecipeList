// pacote onde o arquivo está localizado
package com.example.recipelist.viewmodel

// imports necessários
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// viewmodel para configurações do app
class SettingsViewModel : ViewModel() {

    // tema escuro ativado ou não
    val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    // notificações ativadas ou não
    val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    // troca o tema entre claro e escuro
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // define o tema escuro diretamente
    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
    }

    // troca o estado das notificações
    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
    }

    // define se notificações estão ativadas
    fun setNotifications(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    // reseta preferências para padrão
    fun resetPreferences() {
        _isDarkTheme.value = false
        _notificationsEnabled.value = true
    }
}
