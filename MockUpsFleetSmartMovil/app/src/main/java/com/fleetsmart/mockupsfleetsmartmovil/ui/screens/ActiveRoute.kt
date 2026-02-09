package com.fleetsmart.mockupsfleetsmartmovil.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fleetsmart.mockupsfleetsmartmovil.data.model.Stop
import com.fleetsmart.mockupsfleetsmartmovil.ui.components.AppCard
import com.fleetsmart.mockupsfleetsmartmovil.ui.components.Badge
import com.fleetsmart.mockupsfleetsmartmovil.ui.theme.AppColors
import com.fleetsmart.mockupsfleetsmartmovil.ui.viewmodel.ActiveRouteViewModel

@Composable
fun ActiveRouteScreen(
    onBack: () -> Unit,
    viewModel: ActiveRouteViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.routeName,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                text = "${state.distance} · ${state.duration}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AppColors.MutedForeground
                            )
                        }
                        Badge(
                            text = "${viewModel.completedStops} / ${viewModel.totalStops}"
                        )
                    }

                    // Progress
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progreso de la ruta",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.MutedForeground
                            )
                            Text(
                                text = "${(viewModel.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        LinearProgressIndicator(
                            progress = { viewModel.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(MaterialTheme.shapes.small),
                            color = AppColors.Primary,
                            trackColor = AppColors.Muted
                        )
                    }
                }
            }

            // Map
            item {
                AppCard {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        RouteMapPreview(stops = state.stops)
                    }
                }
            }

            // Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.togglePauseRoute() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (state.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (state.isPaused) "Reanudar" else "Pausar")
                    }

                    Button(
                        onClick = { viewModel.finishRoute() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.Destructive
                        )
                    ) {
                        Text("Finalizar Ruta")
                    }
                }
            }

            // Stops List
            item {
                Text(
                    text = "Paradas de la Ruta",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            itemsIndexed(state.stops) { index, stop ->
                StopCard(
                    stop = stop,
                    index = index,
                    onToggleComplete = { viewModel.toggleStopComplete(stop.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun RouteMapPreview(stops: List<Stop>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2FE))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Dibujar grid
            val gridSpacing = 20.dp.toPx()
            val gridColor = Color(0x0D000000)

            for (x in 0..size.width.toInt() step gridSpacing.toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(x.toFloat(), 0f),
                    end = Offset(x.toFloat(), size.height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..size.height.toInt() step gridSpacing.toInt()) {
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y.toFloat()),
                    end = Offset(size.width, y.toFloat()),
                    strokeWidth = 1f
                )
            }

            // Dibujar ruta
            val points = listOf(
                Offset(size.width * 0.2f, size.height * 0.8f),
                Offset(size.width * 0.35f, size.height * 0.6f),
                Offset(size.width * 0.5f, size.height * 0.48f),
                Offset(size.width * 0.7f, size.height * 0.56f),
                Offset(size.width * 0.85f, size.height * 0.4f)
            )

            val completedPath = Path().apply {
                moveTo(points[0].x, points[0].y)
                lineTo(points[1].x, points[1].y)
            }

            val pendingPath = Path().apply {
                moveTo(points[1].x, points[1].y)
                for (i in 2 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }

            drawPath(
                path = completedPath,
                color = Color(0xFF10B981),
                style = Stroke(width = 4.dp.toPx())
            )

            drawPath(
                path = pendingPath,
                color = Color(0xFF3B82F6),
                style = Stroke(width = 4.dp.toPx())
            )
        }

        // Current location indicator
        Box(
            modifier = Modifier
                .offset(x = 150.dp, y = 120.dp)
                .size(32.dp)
                .background(Color(0xFF3B82F6), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun StopCard(
    stop: Stop,
    index: Int,
    onToggleComplete: () -> Unit
) {
    AppCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onToggleComplete,
                modifier = Modifier.size(40.dp)
            ) {
                if (stop.completed) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completada",
                        tint = AppColors.Success,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Pendiente",
                        tint = AppColors.MutedForeground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column {
                    Text(
                        text = "Parada #${index + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.MutedForeground
                    )
                    Text(
                        text = stop.address,
                        style = MaterialTheme.typography.bodyMedium,
                        textDecoration = if (stop.completed) TextDecoration.LineThrough else null,
                        color = if (stop.completed) AppColors.MutedForeground else AppColors.Foreground
                    )
                }

                if (stop.phone != null && !stop.completed) {
                    OutlinedButton(
                        onClick = { /* Llamar */ },
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Llamar")
                    }
                }
            }
        }
    }
}