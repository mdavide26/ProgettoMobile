package it.unibo.progettomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import it.unibo.progettomobile.ui.ProgettoMobileNavGraph
import it.unibo.progettomobile.ui.theme.ProgettoMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProgettoMobileTheme {
                val navController = rememberNavController()
                ProgettoMobileNavGraph(navController)
            }
        }
    }
}
