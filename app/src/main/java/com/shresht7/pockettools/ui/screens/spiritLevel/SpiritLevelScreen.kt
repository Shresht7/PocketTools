package com.shresht7.pockettools.ui.screens.spiritLevel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shresht7.pockettools.data.Orientation
import com.shresht7.pockettools.data.rememberOrientation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpiritLevelScreen(navController: NavController) {
    val orientation = rememberOrientation()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Spirit Level") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SpiritLevel(orientation)
            Readouts(orientation, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun SpiritLevel(orientation: Orientation) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val bubbleRadius = 40.dp.toPx()
        val maxOffset = size.minDimension / 3f

        fun map(value: Float): Float {
            return (value / 45f).coerceIn(-1f, 1f) * maxOffset
        }

        val bubbleX = size.center.x + map(orientation.roll)
        val bubbleY = size.center.y + map(orientation.pitch)

        // Background Circle
        drawCircle(
            color = Color(0xFF333333),
            radius = maxOffset * 1.2f,
            center = size.center,
            style = Stroke(width = 8.dp.toPx())
        )

        // Center Crosshair
        drawLine(
            color = Color.Gray,
            start = Offset(size.center.x - maxOffset, size.center.y),
            end = Offset(size.center.x + maxOffset, size.center.y),
            strokeWidth = 4.dp.toPx()
        )
        drawLine(
            color = Color.Gray,
            start = Offset(size.center.x, size.center.y - maxOffset),
            end = Offset(size.center.x, size.center.y + maxOffset),
            strokeWidth = 4.dp.toPx()
        )

        // Bubble
        drawCircle(
            color = primaryColor,
            radius = bubbleRadius,
            center = Offset(bubbleX, bubbleY)
        )
    }
}

@Composable
fun Readouts(orientation: Orientation, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Text(
            text = "Roll (x): ${"%.1f".format(orientation.roll)}°",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Pitch (y): ${"%.1f".format(orientation.pitch)}°",
            style = MaterialTheme.typography.titleMedium
        )
    }
}