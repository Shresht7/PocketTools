package com.shresht7.pockettools.ui.screens.spiritLevel

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpiritLevel(
    pitch: Float,
    modifier: Modifier,
    bubbleColor: Color = MaterialTheme.colorScheme.primary,
    bubbleRadius: Dp = 14.dp,
    scaleColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    cornerRadius: CornerRadius = CornerRadius(50f, 50f),
    bubbleTravelRange: Float = 0.5f,
) {
    Canvas(modifier) {
        val barHeight = size.height
        val barWidth = size.width

        val centerX = size.width / 2
        val centerY = size.height / 2

        val maxOffset = barHeight * bubbleTravelRange
        val bubbleY = centerY + mapTiltToOffset(-pitch, maxOffset)
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
            center = Offset(centerX, bubbleY)
        )
    }
}