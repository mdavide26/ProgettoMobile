package it.unibo.progettomobile.ui.screens.moviedetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import it.unibo.progettomobile.data.remote.dto.MovieDTO
import it.unibo.progettomobile.ui.composables.TopBar

@Composable
fun MovieDetailsScreen(
    movie: MovieDTO?,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar(movie?.title ?: "Dettagli", navController, showSearchButton = false) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFavoriteToggle,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (isFavorite) "Rimuovi dai preferiti" else "Aggiungi ai preferiti"
                )
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

                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    IconButton(onClick = {
                        val searchQuery = Uri.encode("${it.title} trailer")
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/results?search_query=$searchQuery")
                        )
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Guarda trailer")
                    }

                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Guarda questo film: ${it.title}\n${it.overview}"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Condividi con"))
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Condividi")
                    }
                }

                Text(
                    text = it.overview,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
