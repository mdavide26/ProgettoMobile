package it.unibo.progettomobile.data.repositories

import it.unibo.progettomobile.data.database.FavoriteMovie
import it.unibo.progettomobile.data.database.MovieDAO
import it.unibo.progettomobile.data.remote.TmdbDataSource
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val dataSource: TmdbDataSource,
    private val movieDao: MovieDAO
) {
    // --- Richieste API (Remote) ---

    suspend fun getPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results
    }

    suspend fun getTopRatedPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results.filter { it.vote_average >= 7.0 }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDTO {
        return dataSource.getMovieDetails(movieId)
    }

    // --- Gestione Preferiti (Locale/Database) ---

    fun getAllFavorites(): Flow<List<FavoriteMovie>> {
        return movieDao.getAllFavorites()
    }

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.isFavorite(movieId)
    }

    suspend fun toggleFavorite(movie: MovieDTO, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            movieDao.deleteFavorite(movie.toFavoriteEntity())
        } else {
            movieDao.insertFavorite(movie.toFavoriteEntity())
        }
    }

    // Funzione di utilità per convertire da DTO (API) a Entity (Database)
    private fun MovieDTO.toFavoriteEntity() = FavoriteMovie(
        id = id,
        title = title,
        posterPath = poster_path,
        overview = overview,
        voteAverage = vote_average
    )
}