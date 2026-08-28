package it.unibo.progettomobile.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.progettomobile.ui.composables.AppBar

@Composable
fun SettingsScreen(
    username: String,
    onUsernameChange: (String) -> Unit,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            AppBar("Settings", navController, showSearchButton = false, showSettingsButton = false)
        }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(contentPadding).padding(12.dp).fillMaxSize()
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(36.dp))
            Text(
                text = username,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
