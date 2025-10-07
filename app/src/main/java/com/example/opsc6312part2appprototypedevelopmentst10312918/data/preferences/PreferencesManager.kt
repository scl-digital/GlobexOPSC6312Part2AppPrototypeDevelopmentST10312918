package com.example.opsc6312part2appprototypedevelopmentst10312918.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val context: Context) {
    
    companion object {
        private val CURRENT_USER_ID = longPreferencesKey("current_user_id")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val THEME_MODE = booleanPreferencesKey("theme_mode") // true for dark, false for light
    }
    
    /**
     * Save current user session
     */
    suspend fun saveUserSession(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_USER_ID] = userId
            preferences[IS_LOGGED_IN] = true
        }
    }
    
    /**
     * Clear user session (logout)
     */
    suspend fun clearUserSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(CURRENT_USER_ID)
            preferences[IS_LOGGED_IN] = false
        }
    }
    
    /**
     * Get current user ID
     */
    fun getCurrentUserId(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            preferences[CURRENT_USER_ID]
        }
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }
    
    /**
     * Save theme preference
     */
    suspend fun saveThemeMode(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = isDarkTheme
        }
    }
    
    /**
     * Get theme preference
     */
    fun getThemeMode(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_MODE] ?: false
        }
    }
}