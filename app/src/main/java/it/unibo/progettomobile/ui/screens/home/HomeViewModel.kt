package it.unibo.progettomobile.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import it.unibo.progettomobile.data.repositories.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val movies: List<MovieDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val movies = repository.getPopularFilms()
                println("MOVIES_DEBUG: Ricevuti ${movies.size} film") // Log di controllo
                _state.update { it.copy(movies = movies, isLoading = false) }
            } catch (e: Exception) {
                println("MOVIES_DEBUG: Errore -> ${e.message}") // Log dell'errore
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}