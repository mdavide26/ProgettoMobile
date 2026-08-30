package it.unibo.progettomobile.ui.screens.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.datastore.SessionManager
import it.unibo.progettomobile.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class AuthState {
    object Idle: AuthState()
    object Loading: AuthState()
    object Success: AuthState()
    data class Error(val message : String) : AuthState()
}

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel () {

    // _authState per la modifica dello stato, authState per solo la lettura che guarda quando viene modificato _authState
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email : String , password : String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.login(email, password)
                .onSuccess {
                    sessionManager.setLoggedIn(email)
                    _authState.value = AuthState.Success
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Errore sconosciuto")
                }
        }
    }

    fun register(email : String, password : String, username : String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            authRepository.register(email, password, username)
                .onSuccess {
                    sessionManager.setLoggedIn(email)
                    _authState.value = AuthState.Success
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Errore sconosciuto")
                }
        }
    }

}