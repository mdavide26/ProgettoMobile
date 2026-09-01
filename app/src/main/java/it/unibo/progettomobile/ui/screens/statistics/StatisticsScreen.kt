package it.unibo.progettomobile.ui.screens.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.progettomobile.ui.composables.BottomBar
import it.unibo.progettomobile.ui.composables.TopBar
import org.koin.androidx.compose.koinViewModel

private val chartColors = listOf(
    Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFFFF6B6B),
    Color(0xFFFFD93D), Color(0xFF6BCB77), Color(0xFF4D96FF),
    Color(0xFFFF9F1C), Color(0xFFE84A5F)
)

@Composable
fun PieChart(data: List<GenreStat>, modifier: Modifier = Modifier) {
    val total = data.sumOf { it.count }.toFloat()
    Canvas(
        modifier = modifier.size(200.dp),
    ) {
        var startAngle = -90f
        data.forEachIndexed { index, stat ->
            val sweepAngle = (stat.count / total) * 360f
            drawArc(
                color = chartColors[index % chartColors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun StatisticsScreen(
    navController: NavHostController,
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val stats by viewModel.genreStats.collectAsState()

    Scaffold(
        topBar = { TopBar("Statistiche", navController, showSearchButton = false) },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Generi preferiti", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            if (stats.isEmpty()) {
                Text("Nessun film nei preferiti ancora")
            } else {
                PieChart(data = stats)
                Spacer(Modifier.height(16.dp))
                stats.forEachIndexed { index, stat ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(chartColors[index % chartColors.size])
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("${stat.genreName}: ${stat.count}")
                    }
                }
            }
        }
    }
}