package com.example.recipelist.data.repository

import com.example.recipelist.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val settingsDataStore: SettingsDataStore) {


    val isDarkTheme: Flow<Boolean> = settingsDataStore.themePreferenceFlow
    val areNotificationsEnabled: Flow<Boolean> = settingsDataStore.notificationsPreferenceFlow


    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        settingsDataStore.saveThemePreference(isDarkTheme)
    }

    suspend fun setNotificationsEnabled(isEnabled: Boolean) {
        settingsDataStore.saveNotificationsPreference(isEnabled)
    }
}