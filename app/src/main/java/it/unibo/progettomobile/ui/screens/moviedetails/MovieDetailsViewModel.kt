package it.unibo.progettomobile.ui.screens.moviedetails

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import it.unibo.progettomobile.data.repositories.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _state = MutableStateFlow<MovieDTO?>(null)
    val state = _state.asStateFlow()

    fun fetchDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(movieId)
                _state.value = movie
            } catch (e: Exception) {
                // Gestisci errore
            }
        }
    }
}
