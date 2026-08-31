package it.unibo.progettomobile.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import it.unibo.progettomobile.data.repositories.MovieRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeState(
    val movies: List<MovieDTO> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        fetchPopularMovies()
                    } else {
                        searchMovies(query)
                    }
                }
        }
    }

    // 3. Questa funzione ora aggiorna solo il flusso locale, non fa chiamate API dirette
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val results = repository.searchMovies(query)
                _state.update { it.copy(movies = results, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val movies = repository.getPopularFilms()
                _state.update { it.copy(movies = movies, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}