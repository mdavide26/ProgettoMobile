// repositories/MovieRepository.kt
package it.unibo.progettomobile.data.repositories

import it.unibo.progettomobile.data.database.dao.MovieDAO
import it.unibo.progettomobile.data.database.entities.Movie
import it.unibo.progettomobile.data.remote.TmdbDataSource
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val dataSource: TmdbDataSource,
    private val movieDao: MovieDAO
) {
    suspend fun getPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results
    }

    suspend fun getTopRatedPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results.filter { it.vote_average >= 7.0 }
    }

    suspend fun addToWatchlist(movieDto: MovieDTO) {
        movieDao.insert(
            Movie(
                tmdbId = movieDto.id,
                title = movieDto.title,
                posterUrl = movieDto.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" }
            )
        )
    }

    fun getWatchlist(): Flow<List<Movie>> {
        return movieDao.getWatchlist()
    }

    suspend fun removeFromWatchlist(movie: Movie) {
        movieDao.delete(movie)
    }
}