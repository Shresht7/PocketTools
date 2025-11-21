package com.shresht7.pockettools.ui.screens.spiritLevel

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A composable that displays a horizontal spirit level.
 *
 * @param roll The roll angle in degrees. The bubble moves based on this value.
 * @param modifier The modifier to be applied to the canvas.
 * @param bubbleColor The color of the bubble.
 * @param bubbleRadius The radius of the bubble.
 * @param scaleColor The color of the vial (the background bar).
 * @param cornerRadius The corner radius for the vial.
 * @param bubbleTravelRange A factor determining how far the bubble can travel from the center. 0.5 means it can travel up to 50% of the bar's width in either direction.
 */
@Composable
fun HorizontalSpiritLevel(
    roll: Float,
    modifier: Modifier,
    bubbleColor: Color = MaterialTheme.colorScheme.primary,
    bubbleRadius: Dp = 14.dp,
    scaleColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    cornerRadius: CornerRadius = CornerRadius(50f, 50f),
    bubbleTravelRange: Float = 0.5f,
) {
    Canvas(modifier) {
        val barWidth = size.width
        val barHeight = size.height

        val centerX = size.width / 2
        val centerY = size.height / 2

        val maxOffset = barWidth * bubbleTravelRange
        val bubbleX = centerX + mapTiltToOffset(roll, maxOffset)
        val bubbleRadiusPx = bubbleRadius.toPx()

        // Draw Vial
        drawRoundRect(
            color = scaleColor,
            topLeft = Offset(centerX - barWidth / 2, centerY - barHeight / 2),
            size = Size(barWidth, barHeight),
            cornerRadius = cornerRadius
        )

        // Draw Bubble
        drawCircle(
            color = bubbleColor,
            radius = bubbleRadiusPx,
            center = Offset(bubbleX, centerY)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalSpiritLevelPreview() {
    MaterialTheme {
        HorizontalSpiritLevel(
            roll = 10f,
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(
                            minWidth = 300.dp.toPx().toInt(),
                            minHeight = 50.dp.toPx().toInt()
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                }
        )
    }
}