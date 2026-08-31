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

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    fun fetchDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(movieId)
                _state.value = movie

                repository.isFavorite(movieId).collect {
                    _isFavorite.value = it
                }
            } catch (e: Exception) {
                println("Errore al MovieDetailsViewModel: " + e.message)
            }
        }
    }

    fun toggleFavorite() {
        val movie = _state.value ?: return
        viewModelScope.launch {
            // Usa il repository per aggiungere/rimuovere
            repository.toggleFavorite(movie, _isFavorite.value)
        }
    }
}
