package com.shresht7.pockettools.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shresht7.pockettools.navigation.Screen
import com.shresht7.pockettools.navigation.Screen.Counter.icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    /* The list of tools (in order) to show on the home-page */
    val screens = listOf(
        Screen.Ruler,
        Screen.Torch,
        Screen.Magnetometer,
        Screen.Counter,
        Screen.TipCalculator,
        Screen.SensorsList,
    )

    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        started = true
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 48.dp, 24.dp, 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("Pocket Tools", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            items(screens) { screen ->
                val index = screens.indexOf(screen)
                val delay = index * 80

                val alpha by animateFloatAsState(
                    targetValue = if (started) 1f else 0f,
                    animationSpec = tween(durationMillis = 200, delayMillis = delay)
                )

                val offsetY by animateDpAsState(
                    targetValue = if (started) 0.dp else 20.dp,
                    animationSpec = tween(durationMillis = 200, delayMillis = delay)
                )

                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            this.alpha = alpha
                            translationY = offsetY.toPx()
                        }
                ) {
                    ToolCard(
                        imageVector = screen.icon,
                        onClick = { navController.navigate(screen) }
                    ) {
                        Text(
                            text = screen.title,
                            modifier = Modifier.padding(16.dp),
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavController(LocalContext.current))
}