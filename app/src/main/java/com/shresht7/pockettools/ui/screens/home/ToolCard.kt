package com.shresht7.pockettools.ui.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shresht7.pockettools.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A composable function that displays an interactive card for a single tool.
 *
 * This card features a title, an optional background icon, and interactive press animations.
 * When pressed, the card scales down, its elevation changes, and its alpha reduces slightly,
 * providing visual feedback to the user before navigating to the corresponding tool screen.
 *
 * @param title The title of the tool to be displayed on the card.
 * @param onClick A callback function that is invoked when the card is clicked.
 * @param imageVector An optional [ImageVector] to be displayed as a background icon on the card.
 *                    If `null`, no icon is displayed.
 * @param borderStroke The [BorderStroke] to apply to the card's outline.
 * @param shape The [Shape] of the card, defining its corners.
 */
@Composable
fun ToolCard(
    title: String,
    onClick: () -> Unit,
    imageVector: ImageVector? = null,
    borderStroke: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    shape: Shape = MaterialTheme.shapes.medium,
    // content: @Composable () -> Unit,
) {
    var cardWidth by remember { mutableFloatStateOf(0f) }
    var cardHeight by remember { mutableFloatStateOf(0f) }

    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, animationSpec = tween(120))
    val elevation by animateDpAsState(if (pressed) 0.dp else 1.dp, animationSpec = tween(120))
    val alpha by animateFloatAsState(targetValue = if (pressed) 0.85f else 1f, animationSpec = tween(120))

    val scope = rememberCoroutineScope()

    OutlinedCard(
        onClick = {
            scope.launch {
                pressed = true
                delay(130)
                pressed = false
                onClick()
            }
        },
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .fillMaxWidth()
            .padding(8.dp)
            .aspectRatio(1.6f)
            .onGloballyPositioned({ coordinates ->
                val size = coordinates.size
                cardWidth = size.width.toFloat()
                cardHeight = size.height.toFloat()
            })
            .border(borderStroke, shape),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    shadowElevation = elevation.toPx()
                }
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                        ),
                    )
                )
        ) {

            // Background Icon
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = stringResource(R.string.tool_icon_content_description, title),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.20f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                        .size((cardWidth * 0.75).dp)
                        .rotate(12f)
                        .offset(
                            x = (cardWidth * 0.15f).dp,
                            y = (cardHeight * 0.1f).dp,
                        )
                )
            }

            // Tool Card Title
            Text(
                text = title,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToolCardPreview() {
    ToolCard(
        title = "Counter",
        imageVector = Icons.Default.Sensors,
        onClick = {}
    )
}