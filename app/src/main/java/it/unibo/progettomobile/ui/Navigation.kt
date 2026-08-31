package it.unibo.progettomobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import it.unibo.progettomobile.data.datastore.SessionManager
import it.unibo.progettomobile.ui.screens.favorites.FavoritesScreen
import it.unibo.progettomobile.ui.screens.favorites.FavoritesViewModel
import it.unibo.progettomobile.ui.screens.home.HomeScreen
import it.unibo.progettomobile.ui.screens.home.HomeViewModel
import it.unibo.progettomobile.ui.screens.moviedetails.MovieDetailsScreen
import it.unibo.progettomobile.ui.screens.moviedetails.MovieDetailsViewModel
import it.unibo.progettomobile.ui.screens.settings.SettingsScreen
import it.unibo.progettomobile.ui.screens.settings.SettingsViewModel
import it.unibo.progettomobile.ui.screens.statistics.StatisticsScreen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

sealed interface ProgettoMobileRoute {
    @Serializable data object Statistics : ProgettoMobileRoute
    @Serializable data object Home : ProgettoMobileRoute
    @Serializable data object Settings : ProgettoMobileRoute
    @Serializable data object Favorites : ProgettoMobileRoute
    @Serializable data class MovieDetails(val movieId: Int) : ProgettoMobileRoute
}

@Composable
fun ProgettoMobileNavGraph(
    navController: NavHostController,
    sessionManager: SessionManager = koinInject()
) {
    var startDestination by remember { mutableStateOf<Any?>(null) }

    LaunchedEffect(Unit) {
        startDestination = if (sessionManager.isLoggedIn()) {
            ProgettoMobileRoute.Home
        } else {
            AuthRoute.Login
        }
    }

    startDestination?.let { start ->
        NavHost(
            navController = navController,
            startDestination = start
        ) {
            authNavGraph(
                navController = navController,
                onAuthSuccess = {
                    navController.navigate(ProgettoMobileRoute.Home) {
                        popUpTo(AuthRoute.Login) { inclusive = true }
                    }
                }
            )

            composable<ProgettoMobileRoute.Home> {
                val homeVm = koinViewModel<HomeViewModel>()
                val state by homeVm.state.collectAsStateWithLifecycle()
                HomeScreen(
                    state = state,
                    navController = navController,
                    onSearch = { homeVm.onSearchQueryChange(it) }
                )
            }

            composable<ProgettoMobileRoute.MovieDetails> { backStackEntry ->
                val route = backStackEntry.toRoute<ProgettoMobileRoute.MovieDetails>()
                val detailsVm = koinViewModel<MovieDetailsViewModel>()

                LaunchedEffect(route.movieId) {
                    detailsVm.fetchDetails(route.movieId)
                }

                val state by detailsVm.state.collectAsStateWithLifecycle()
                val isFavorite by detailsVm.isFavorite.collectAsStateWithLifecycle()
                MovieDetailsScreen(
                    movie = state,
                    isFavorite = isFavorite,
                    onFavoriteToggle = { detailsVm.toggleFavorite() },
                    navController = navController
                )
            }

            composable<ProgettoMobileRoute.Statistics> {
                StatisticsScreen(navController)
            }

            composable<ProgettoMobileRoute.Settings> {
                val settingsVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(settingsVm.username, settingsVm::updateUsername, navController)
            }

            composable<ProgettoMobileRoute.Favorites> {
                val favoritesVm = koinViewModel<FavoritesViewModel>()
                val favoriteMovies by favoritesVm.favoriteMovies.collectAsStateWithLifecycle()

                FavoritesScreen(
                    favorites = favoriteMovies,
                    navController = navController
                )
            }
        }
    }
}
