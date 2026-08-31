package it.unibo.progettomobile.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.progettomobile.data.repositories.MovieRepository
import it.unibo.progettomobile.utils.GenreMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GenreStat(val genreName: String, val count: Int)

class StatisticsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _genreStats = MutableStateFlow<List<GenreStat>>(emptyList())
    val genreStats: StateFlow<List<GenreStat>> = _genreStats

    init {
        viewModelScope.launch {
            repository.getAllFavorites().collect { favorites ->
                _genreStats.value = favorites
                    .groupBy { GenreMapper.getName(it.genreId) }
                    .map { (genre, movies) -> GenreStat(genre, movies.size) }
                    .sortedByDescending { it.count }
            }
        }
    }
}