package it.unibo.progettomobile.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import it.unibo.progettomobile.data.remote.TmdbDataSource
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertTrue

class MovieRepositoryTest {

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val dataSource = TmdbDataSource(httpClient)

    @Test
    fun `getPopularFilms restituisce una lista non vuota`() = runTest {
        val result = dataSource.getPopularFilms().results
        assertTrue(result.isNotEmpty(), "La lista di film popolari non dovrebbe essere vuota")
        println("RISULTATO: ${result}")
    }

    @Test
    fun `getTopRatedPopularFilms restituisce solo film con voto maggiore o uguale a 7`() = runTest {
        val result = dataSource.getPopularFilms().results.filter { it.vote_average >= 7.0 }
        assertTrue(result.isNotEmpty(), "Ci si aspetta almeno un film con voto alto tra i popolari")
        assertTrue(result.all { it.vote_average >= 7.0 }, "Tutti i film restituiti devono avere voto >= 7.0")
        println("RISULTATO: ${result}")
        println("LUNGHEZZA: ${result.size}")
    }
}