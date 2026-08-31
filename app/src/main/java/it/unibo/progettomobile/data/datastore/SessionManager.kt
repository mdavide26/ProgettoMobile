package it.unibo.progettomobile.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import it.unibo.progettomobile.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


private val LOGGED_IN_EMAIL = stringPreferencesKey("logged_in_email")
class SessionManager (private val context: Context){

    val loggedInEmail: Flow<String?> = context.dataStore.data.map { prefs -> prefs[LOGGED_IN_EMAIL] }

    suspend fun setLoggedIn(email: String) {
        context.dataStore.edit { prefs -> prefs[LOGGED_IN_EMAIL] = email }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs -> prefs.remove(LOGGED_IN_EMAIL) }
    }

    suspend fun isLoggedIn(): Boolean = loggedInEmail.firstOrNull() != null

}