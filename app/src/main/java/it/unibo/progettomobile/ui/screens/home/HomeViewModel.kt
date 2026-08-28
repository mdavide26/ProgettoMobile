package it.unibo.progettomobile.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.database.Trip
import it.unibo.progettomobile.data.repositories.TripsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeState(val trips: List<Trip>)

class HomeViewModel(repository: TripsRepository) : ViewModel() {
    val state = repository.trips.map { HomeState(trips = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeState(emptyList())
    )
}
