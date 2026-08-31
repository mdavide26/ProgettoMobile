package it.unibo.progettomobile.ui.screens.moviedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // O un'altra icona a tua scelta
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import it.unibo.progettomobile.ui.composables.TopBar

@Composable
fun MovieDetailsScreen(movie: MovieDTO?, navController: NavHostController) {
    Scaffold(
        topBar = { TopBar(movie?.title ?: "Dettagli", navController) },
        // AGGIUNTO: Il bottone volante
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    /* Qui metterai l'azione, ad esempio aggiungere ai preferiti o a un diario */
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Aggiungi ai preferiti")
            }
        }
    ) { padding ->
        movie?.let {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${it.poster_path}",
                    contentDescription = it.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = it.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = it.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}