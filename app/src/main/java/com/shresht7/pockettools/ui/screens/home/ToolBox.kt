package com.shresht7.pockettools.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shresht7.pockettools.navigation.Screen
import com.shresht7.pockettools.navigation.Screen.Counter.icon

@Composable
fun ToolBox(
    filtered: List<Screen>,
    screens: List<Screen>,
    onNavigate: (Screen) -> Unit,
    started: Boolean
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        items(filtered) { screen ->
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
                    title = screen.title,
                    imageVector = screen.icon,
                    onClick = { onNavigate(screen) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToolBoxPreview() {
    val screens = listOf(
        Screen.Ruler,
        Screen.Torch,
        Screen.Magnetometer,
        Screen.Counter,
        Screen.TipCalculator
    )
    val filtered = screens.filter { it.title.contains("r", true) }
    ToolBox(filtered, screens, {}, true)
}