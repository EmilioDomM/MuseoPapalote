package com.example.museopapalote

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension to create DataStore
private val Context.dataStore by preferencesDataStore("user_preferences")

class DataStoreManager(private val context: Context) {
    companion object {
        private val LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }

    // Get login state as Flow
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[LOGGED_IN_KEY] ?: false
    }

    // Save login state
    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_KEY] = loggedIn
        }
    }
}
