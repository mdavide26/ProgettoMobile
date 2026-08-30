package it.unibo.progettomobile

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import it.unibo.progettomobile.data.LocationService
import it.unibo.progettomobile.data.database.TravelDiaryDatabase
import it.unibo.progettomobile.data.repositories.SettingsRepository
import it.unibo.progettomobile.data.repositories.TripsRepository
import it.unibo.progettomobile.ui.screens.addtravel.AddTravelViewModel
import it.unibo.progettomobile.ui.screens.home.HomeViewModel
import it.unibo.progettomobile.ui.screens.settings.SettingsViewModel
import it.unibo.progettomobile.ui.screens.traveldetails.TravelDetailsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import it.unibo.progettomobile.data.remote.OSMDataSource
import it.unibo.progettomobile.data.remote.TmdbDataSource
import it.unibo.progettomobile.data.repositories.MovieRepository
import it.unibo.progettomobile.ui.screens.favorites.FavoritesViewModel
import it.unibo.progettomobile.ui.screens.moviedetails.MovieDetailsViewModel
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    // Data sources

    single { get<Context>().dataStore }

    single { TmdbDataSource(get()) }

    single {
        Room.databaseBuilder(
            get(),
            TravelDiaryDatabase::class.java,
            "ProgettoMobile"
        )
        .fallbackToDestructiveMigration(true)
        .build()
    }

    single {
        HttpClient {
            defaultRequest {
                headers.append(
                    HttpHeaders.UserAgent,
                    "HTTPApp/1.0 (com.example.http; http-app)"
                )
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { OSMDataSource(get()) }

    single { LocationService(get()) }

    // Repositories

    single { TripsRepository(get(), get<Context>().contentResolver) }

    single { SettingsRepository(get()) }

    single { get<TravelDiaryDatabase>().movieDAO() }

    single { MovieRepository(get(), get()) }

    // ViewModels

    viewModel { HomeViewModel(get()) }

    viewModel { TravelDetailsViewModel(get()) }

    viewModel { MovieDetailsViewModel(get()) }

    viewModel { FavoritesViewModel(get()) }

    viewModel { AddTravelViewModel(get()) }

    viewModel { SettingsViewModel(get()) }
}
