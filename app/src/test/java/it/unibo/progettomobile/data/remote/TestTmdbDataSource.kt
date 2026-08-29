package it.unibo.progettomobile.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import it.unibo.progettomobile.BuildConfig
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class TmdbDataSourceTest {

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val dataSource = TmdbDataSource(httpClient)

    @Test
    fun `debug - stampa risposta grezza`() = runTest {
        println("TOKEN LETTO: ${BuildConfig.TMDB_API_TOKEN.take(15)}...")

        val response: HttpResponse = httpClient.get("https://api.themoviedb.org/3/movie/popular?language=it-IT") {
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.TMDB_API_TOKEN}")
        }

        println("STATUS: ${response.status}")
        println("BODY: ${response.bodyAsText()}")
        println("CLASS: ${response.javaClass}")
    }

    @Test
    fun `getPopularFilms restituisce una lista non vuota`() = runTest {
        val result = dataSource.getPopularFilms()
        kotlin.test.assertTrue(result.results.isNotEmpty(), "La lista di film popolari non dovrebbe essere vuota")
    }
}