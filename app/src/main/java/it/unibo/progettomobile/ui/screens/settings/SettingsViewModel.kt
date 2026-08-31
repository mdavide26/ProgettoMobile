package it.unibo.progettomobile.ui.screens.settings

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.database.dao.UserDAO
import it.unibo.progettomobile.data.database.entities.User
import it.unibo.progettomobile.data.datastore.SessionManager
import it.unibo.progettomobile.data.repositories.SettingsRepository
import it.unibo.progettomobile.ui.theme.ThemeMode
import it.unibo.progettomobile.utils.PasswordHasher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(
    private val userDAO: UserDAO,
    private val sessionManager: SessionManager,
    private val repository: SettingsRepository
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = repository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    var email by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var profilePictureUri by mutableStateOf<String?>(null)
        private set

    var newPassword by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var isSaving by mutableStateOf(false)
        private set

    var userMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            isLoading = true
            val currentEmail = sessionManager.loggedInEmail.firstOrNull()
            if (!currentEmail.isNullOrEmpty()) {
                email = currentEmail
                var user = userDAO.getUserByEmail(currentEmail)
                if (user == null) {
                    val defaultUsername = repository.username.firstOrNull()?.ifBlank { null }
                        ?: currentEmail.substringBefore("@")
                    user = User(
                        email = currentEmail,
                        passwordHashed = "",
                        username = defaultUsername
                    )
                    userDAO.addUser(user)
                }
                username = user.username
                profilePictureUri = user.profilePictureUri
            }
            isLoading = false
        }
    }

    fun onUsernameChange(value: String) {
        username = value
    }

    fun onNewPasswordChange(value: String) {
        newPassword = value
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            repository.setThemeMode(mode)
        }
    }

    fun updateProfilePicture(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val currentEmail = email.ifBlank {
                    sessionManager.loggedInEmail.firstOrNull() ?: ""
                }
                if (currentEmail.isBlank()) {
                    userMessage = "Utente non identificato."
                    return@launch
                }

                context.filesDir.listFiles { _, name ->
                    name.startsWith("profile_${currentEmail.hashCode()}")
                }?.forEach { it.delete() }

                val timeStamp = System.currentTimeMillis()
                val file = File(context.filesDir, "profile_${currentEmail.hashCode()}_$timeStamp.jpg")

                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                val newUriString = Uri.fromFile(file).toString()

                val existingUser = userDAO.getUserByEmail(currentEmail)
                val userToSave = existingUser?.copy(profilePictureUri = newUriString)
                    ?: User(
                        email = currentEmail,
                        passwordHashed = "",
                        username = username.ifEmpty { currentEmail.substringBefore("@") },
                        profilePictureUri = newUriString
                    )

                userDAO.addUser(userToSave)
                profilePictureUri = newUriString
                userMessage = "Foto profilo aggiornata con successo!"
            } catch (e: Exception) {
                userMessage = "Errore durante il salvataggio dell'immagine: ${e.localizedMessage}"
            }
        }
    }

    fun removeProfilePicture(context: Context) {
        viewModelScope.launch {
            try {
                val currentEmail = email.ifBlank {
                    sessionManager.loggedInEmail.firstOrNull() ?: ""
                }
                if (currentEmail.isNotBlank()) {
                    context.filesDir.listFiles { _, name ->
                        name.startsWith("profile_${currentEmail.hashCode()}")
                    }?.forEach { it.delete() }
                }

                val existingUser = userDAO.getUserByEmail(currentEmail)
                if (existingUser != null) {
                    val updatedUser = existingUser.copy(profilePictureUri = null)
                    userDAO.updateUser(updatedUser)
                }
                profilePictureUri = null
                userMessage = "Foto profilo rimossa."
            } catch (e: Exception) {
                userMessage = "Errore durante la rimozione dell'immagine: ${e.localizedMessage}"
            }
        }
    }

    fun saveChanges() {
        if (email.isBlank()) {
            userMessage = "Impossibile trovare l'utente corrente."
            return
        }

        viewModelScope.launch {
            isSaving = true
            userMessage = null
            try {
                val existingUser = userDAO.getUserByEmail(email)
                val updatedPasswordHash = if (newPassword.isNotBlank()) {
                    PasswordHasher.hash(newPassword)
                } else {
                    existingUser?.passwordHashed ?: ""
                }

                val userToSave = existingUser?.copy(
                    username = username,
                    passwordHashed = updatedPasswordHash
                ) ?: User(
                    email = email,
                    passwordHashed = updatedPasswordHash,
                    username = username,
                    profilePictureUri = profilePictureUri
                )

                userDAO.addUser(userToSave)
                repository.setUsername(username)

                newPassword = ""
                userMessage = "Proprietà utente aggiornate con successo!"
            } catch (e: Exception) {
                userMessage = "Errore durante il salvataggio: ${e.localizedMessage}"
            } finally {
                isSaving = false
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            sessionManager.logout()
            onComplete()
        }
    }

    fun clearUserMessage() {
        userMessage = null
    }
}
