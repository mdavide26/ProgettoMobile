package it.unibo.progettomobile.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import it.unibo.progettomobile.data.database.MovieDAO
import it.unibo.progettomobile.data.database.entities.FavoriteMovie
import it.unibo.progettomobile.data.datastore.SessionManager
import it.unibo.progettomobile.data.remote.TmdbDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertTrue

// Fake minimale, sufficiente per far compilare ed eseguire i test di rete
private class FakeMovieDAO : MovieDAO {
    override fun getAllFavorites(userEmail: String): Flow<List<FavoriteMovie>> = flowOf(emptyList())
    override suspend fun insertFavorite(movie: FavoriteMovie) {}
    override suspend fun deleteFavorite(movie: FavoriteMovie) {}
    override fun isFavorite(id: Int, userEmail: String): Flow<Boolean> = flowOf(false)
}

class MovieRepositoryTest {

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val repository = MovieRepository(
        TmdbDataSource(httpClient),
        FakeMovieDAO(),
        SessionManager(context = TODO())   // vedi nota sotto
    )

    @Test
    fun `getPopularFilms restituisce una lista non vuota`() = runTest {
        val result = repository.getPopularFilms()
        assertTrue(result.isNotEmpty(), "La lista di film popolari non dovrebbe essere vuota")
        println("RISULTATO: ${result}")
    }

    @Test
    fun `getTopRatedPopularFilms restituisce solo film con voto maggiore o uguale a 7`() = runTest {
        val result = repository.getTopRatedPopularFilms()
        assertTrue(result.isNotEmpty(), "Ci si aspetta almeno un film con voto alto tra i popolari")
        assertTrue(result.all { it.vote_average >= 7.0 }, "Tutti i film restituiti devono avere voto >= 7.0")
        println("RISULTATO: ${result}")
        println("LUNGHEZZA: ${result.size}")
    }
}