package it.unibo.progettomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import it.unibo.progettomobile.data.repositories.SettingsRepository
import it.unibo.progettomobile.ui.ProgettoMobileNavGraph
import it.unibo.progettomobile.ui.theme.ProgettoMobileTheme
import it.unibo.progettomobile.ui.theme.ThemeMode
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsRepository: SettingsRepository = koinInject()
            val themeMode by settingsRepository.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

            ProgettoMobileTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                ProgettoMobileNavGraph(navController)
            }
        }
    }
}
