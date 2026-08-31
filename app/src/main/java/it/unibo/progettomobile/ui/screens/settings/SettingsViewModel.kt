package it.unibo.progettomobile.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.datastore.SessionManager
import it.unibo.progettomobile.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val sessionManager: SessionManager   // <- AGGIUNTO: nuova dipendenza
) : ViewModel() {
    // We are using Compose state directly inside the ViewModel
    // Pro: no need to use .collectAsStateWithLifecycle() in the UI
    // Cons: not thread-safe, ties the ViewModel to Jetpack Compose
    var username by mutableStateOf((""))
        private set

    fun updateUsername(value: String) {
        username = value
        viewModelScope.launch {
            repository.setUsername(value)
        }
    }

    // AGGIUNTO: nuova funzione
    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            sessionManager.logout()
            onComplete()
        }
    }

    init {
        viewModelScope.launch {
            username = repository.username.first()
        }
    }
}