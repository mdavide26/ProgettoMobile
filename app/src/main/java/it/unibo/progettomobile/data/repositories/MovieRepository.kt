package it.unibo.progettomobile.data.repositories

import it.unibo.progettomobile.data.database.MovieDAO
import it.unibo.progettomobile.data.database.entities.FavoriteMovie
import it.unibo.progettomobile.data.datastore.SessionManager
import it.unibo.progettomobile.data.remote.TmdbDataSource
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class MovieRepository(
    private val dataSource: TmdbDataSource,
    private val movieDao: MovieDAO,
    private val sessionManager: SessionManager
) {
    suspend fun getPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results
    }

    suspend fun getTopRatedPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results.filter { it.vote_average >= 7.0 }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDTO {
        return dataSource.getMovieDetails(movieId)
    }

    suspend fun searchMovies(query: String): List<MovieDTO> {
        return dataSource.searchMovies(query).results
    }

    fun getAllFavorites(): Flow<List<FavoriteMovie>> {
        return sessionManager.loggedInEmail.flatMapLatest { email ->
            if (email != null) movieDao.getAllFavorites(email) else flowOf(emptyList())
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return sessionManager.loggedInEmail.flatMapLatest { email ->
            if (email != null) movieDao.isFavorite(movieId, email) else flowOf(false)
        }
    }

    suspend fun toggleFavorite(movie: MovieDTO, isCurrentlyFavorite: Boolean) {
        val email = sessionManager.loggedInEmail.first() ?: return
        if (isCurrentlyFavorite) {
            movieDao.deleteFavorite(movie.toFavoriteEntity(email))
        } else {
            movieDao.insertFavorite(movie.toFavoriteEntity(email))
        }
    }

    private fun MovieDTO.toFavoriteEntity(userEmail: String) = FavoriteMovie(
        id = id,
        title = title,
        posterPath = poster_path,
        overview = overview,
        voteAverage = vote_average,
        genreId = genre_ids.firstOrNull(),
        userEmail = userEmail
    )
}