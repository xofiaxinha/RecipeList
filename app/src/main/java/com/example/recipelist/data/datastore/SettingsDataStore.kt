package com.example.recipelist.data.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

       private object PreferencesKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }


    val themePreferenceFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->

            preferences[PreferencesKeys.IS_DARK_MODE] ?: false
        }


    suspend fun saveThemePreference(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = isDarkMode
        }
    }


    val notificationsPreferenceFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->

            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
        }

    suspend fun saveNotificationsPreference(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = isEnabled
        }
    }
}