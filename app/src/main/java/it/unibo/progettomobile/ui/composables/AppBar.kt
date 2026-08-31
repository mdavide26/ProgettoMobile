package it.unibo.progettomobile.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.unibo.progettomobile.ui.ProgettoMobileRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navController: NavHostController,
    showSearchButton: Boolean = true
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    CenterAlignedTopAppBar(
        title = {
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search a film..") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            } else {
                Text(
                    title,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        navigationIcon = {
            if (isSearchActive) {
                IconButton(onClick = {
                    isSearchActive = false
                    searchQuery = ""
                }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Chiudi ricerca"
                    )
                }
            } else if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        "Go Back"
                    )
                }
            }
        },
        actions = {
            if (isSearchActive) {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Outlined.Clear, contentDescription = "Cancella testo")
                    }
                }
            } else {
                if (showSearchButton) {
                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
    showHomeButton: Boolean = true,
    showFavoritesButton: Boolean = true,
    showStatisticsButton: Boolean = true,
    showSettingsButton: Boolean = true,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        if (showHomeButton) {
            val isHomeSelected = navBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute<ProgettoMobileRoute.Home>()
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isHomeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home"
                    )
                },
                label = { Text("Home") },
                selected = isHomeSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    if (!isHomeSelected) {
                        navController.navigate(ProgettoMobileRoute.Home) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        if (showFavoritesButton) {
            val isFavoritesSelected = navBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute<ProgettoMobileRoute.Favorites>()
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isFavoritesSelected) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Preferiti"
                    )
                },
                label = { Text("Preferiti") },
                selected = isFavoritesSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    if (!isFavoritesSelected) {
                        navController.navigate(ProgettoMobileRoute.Favorites) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        if (showStatisticsButton) {
            val isStatisticsSelected = navBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute<ProgettoMobileRoute.Statistics>()
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isStatisticsSelected) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                        contentDescription = "Statistiche"
                    )
                },
                label = { Text("Statistiche") },
                selected = isStatisticsSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    if (!isStatisticsSelected) {
                        navController.navigate(ProgettoMobileRoute.Statistics) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        if (showSettingsButton) {
            val isSettingsSelected = navBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute<ProgettoMobileRoute.Settings>()
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSettingsSelected) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = "Settings"
                    )
                },
                label = { Text("Settings") },
                selected = isSettingsSelected,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    if (!isSettingsSelected) {
                        navController.navigate(ProgettoMobileRoute.Settings) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}