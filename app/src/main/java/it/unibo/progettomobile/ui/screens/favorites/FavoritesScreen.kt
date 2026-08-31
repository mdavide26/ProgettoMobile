package it.unibo.progettomobile.ui.screens.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.progettomobile.ui.ProgettoMobileRoute
import it.unibo.progettomobile.ui.composables.BottomBar
import it.unibo.progettomobile.ui.composables.TopBar
import it.unibo.progettomobile.ui.screens.home.MovieItem // Riutilizziamo il componente della Home
import it.unibo.progettomobile.data.database.FavoriteMovie
import it.unibo.progettomobile.data.remote.dto.MovieDTO

@Composable
fun FavoritesScreen(
    favorites: List<FavoriteMovie>,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar("I miei Preferiti", navController) },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Non hai ancora aggiunto film preferiti")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(favorites) { fav ->
                    // Convertiamo FavoriteMovie in MovieDTO per riutilizzare MovieItem
                    val movieDto = MovieDTO(fav.id, fav.title, fav.overview, fav.posterPath, fav.voteAverage)
                    MovieItem(movieDto, onClick = {
                        navController.navigate(ProgettoMobileRoute.MovieDetails(fav.id))
                    })
                }
            }
        }
    }
}