package it.unibo.progettomobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import it.unibo.progettomobile.ui.screens.addtravel.AddTravelScreen
import it.unibo.progettomobile.ui.screens.addtravel.AddTravelViewModel
import it.unibo.progettomobile.ui.screens.home.HomeScreen
import it.unibo.progettomobile.ui.screens.home.HomeViewModel
import it.unibo.progettomobile.ui.screens.settings.SettingsScreen
import it.unibo.progettomobile.ui.screens.settings.SettingsViewModel
import it.unibo.progettomobile.ui.screens.traveldetails.TravelDetailsScreen
import it.unibo.progettomobile.ui.screens.traveldetails.TravelDetailsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface TravelDiaryRoute {
    @Serializable data object Home : TravelDiaryRoute
    @Serializable data class TravelDetails(val travelId: Int) : TravelDiaryRoute
    @Serializable data object AddTravel : TravelDiaryRoute
    @Serializable data object Settings : TravelDiaryRoute
}

@Composable
fun TravelDiaryNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TravelDiaryRoute.Home
    ) {
        composable<TravelDiaryRoute.Home> {
            val homeVm = koinViewModel<HomeViewModel>()
            val state by homeVm.state.collectAsStateWithLifecycle()
            HomeScreen(state, navController)
        }
        composable<TravelDiaryRoute.TravelDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<TravelDiaryRoute.TravelDetails>()
            val travelDetailsVm = koinViewModel<TravelDetailsViewModel>()
            travelDetailsVm.tripId.value = route.travelId
            val state by travelDetailsVm.trip.collectAsStateWithLifecycle()
            state?.let { TravelDetailsScreen(navController, it) }
        }
        composable<TravelDiaryRoute.AddTravel> {
            val addTravelVm = koinViewModel<AddTravelViewModel>()
            val state by addTravelVm.state.collectAsStateWithLifecycle()
            AddTravelScreen(state, addTravelVm.actions, navController)
        }
        composable<TravelDiaryRoute.Settings> {
            val settingsVm = koinViewModel<SettingsViewModel>()
            SettingsScreen(settingsVm.username, settingsVm::updateUsername, navController)
        }
    }
}
