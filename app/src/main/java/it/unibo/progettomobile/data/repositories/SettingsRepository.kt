package it.unibo.progettomobile.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import it.unibo.progettomobile.ui.theme.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }

    val username = dataStore.data.map { it[USERNAME_KEY] ?: "" }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { preferences ->
        val modeName = preferences[THEME_MODE_KEY]
        if (modeName != null) {
            try {
                ThemeMode.valueOf(modeName)
            } catch (e: IllegalArgumentException) {
                ThemeMode.SYSTEM
            }
        } else {
            ThemeMode.SYSTEM
        }
    }

    suspend fun setUsername(username: String) = dataStore.edit { it[USERNAME_KEY] = username }

    suspend fun setThemeMode(themeMode: ThemeMode) = dataStore.edit { preferences ->
        preferences[THEME_MODE_KEY] = themeMode.name
    }
}
