// repositories/MovieRepository.kt
package it.unibo.progettomobile.data.repositories

import it.unibo.progettomobile.data.remote.TmdbDataSource
import it.unibo.progettomobile.data.remote.dto.MovieDTO

class MovieRepository(
    private val dataSource: TmdbDataSource
) {
    suspend fun getPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results
    }

    suspend fun getTopRatedPopularFilms(): List<MovieDTO> {
        return dataSource.getPopularFilms().results.filter { it.vote_average >= 7.0 }
    }
}