package it.unibo.progettomobile.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import it.unibo.progettomobile.BuildConfig
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import it.unibo.progettomobile.data.remote.dto.MovieDetailDTO
import it.unibo.progettomobile.data.remote.dto.MovieListDTO

class TmdbDataSource(private val httpClient: HttpClient) {

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3"
    }

    suspend fun getPopularFilms(): MovieListDTO {
        val url = "$BASE_URL/movie/popular?language=it"
        return httpClient.get(url) {
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_API_TOKEN}")
        }.body()
    }

    suspend fun getMovieDetails(movieId: Int): MovieDTO {
        val url = "$BASE_URL/movie/$movieId?language=it"
        val response: MovieDetailDTO = httpClient.get(url) {
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_API_TOKEN}")
            header(HttpHeaders.Accept, "application/json")
        }.body()

        return MovieDTO(
            id = response.id,
            title = response.title,
            overview = response.overview,
            poster_path = response.poster_path,
            vote_average = response.vote_average,
            genre_ids = response.genres.map { it.id }
        )
    }

    suspend fun searchMovies(query: String) : MovieListDTO {
        return httpClient.get("$BASE_URL/search/movie") {
            url {
                parameters.append("query", query)
                parameters.append("language", "it")
            }
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_API_TOKEN}")
        }.body()
    }
}