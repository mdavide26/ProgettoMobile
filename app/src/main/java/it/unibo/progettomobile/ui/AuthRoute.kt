package it.unibo.progettomobile.ui

import kotlinx.serialization.Serializable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import it.unibo.progettomobile.ui.screens.authentication.LoginScreen
import it.unibo.progettomobile.ui.screens.authentication.RegisterScreen

sealed interface AuthRoute {
    @Serializable data object Login : AuthRoute

    @Serializable data object Register : AuthRoute
}

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    onAuthSuccess: () -> Unit
) {
    composable<AuthRoute.Login> {
        LoginScreen(
            onLoginSuccess = onAuthSuccess,
            onNavigateToRegister = { navController.navigate(AuthRoute.Register) }
        )
    }
    composable<AuthRoute.Register> {
        RegisterScreen(
            onRegisterSuccess = onAuthSuccess,
            onNavigateToLogin = { navController.navigate(AuthRoute.Login) }
        )
    }
}