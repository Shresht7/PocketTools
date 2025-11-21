package com.shresht7.pockettools.ui.screens.spiritLevel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shresht7.pockettools.data.Orientation

@Composable
fun SpiritLevel(
    orientation: Orientation,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    crosshairColor: Color = MaterialTheme.colorScheme.onSurface,
    crosshairStrokeDp: Dp = 2.dp,
    bubbleRadiusDp: Dp = 25.dp,
    borderCircleColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
    borderCircleStrokeWidthDp: Dp = 4.dp,
) {
    Canvas(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        val bubbleRadius = bubbleRadiusDp.toPx()
        val crosshairStroke = crosshairStrokeDp.toPx()
        val maxOffset = size.minDimension / 3f

        fun map(value: Float): Float {
            return (value / 45f).coerceIn(-1f, 1f) * maxOffset
        }

        val bubbleX = size.center.x + map(orientation.roll)
        val bubbleY = size.center.y + map(orientation.pitch)

        // Background Circle
        drawCircle(
            color = borderCircleColor,
            radius = maxOffset * 1.2f,
            center = size.center,
            style = Stroke(width = borderCircleStrokeWidthDp.toPx())
        )

        // Center Crosshair
        drawCircle(
            color = crosshairColor.copy(alpha = 0.15f),
            radius = bubbleRadius * 1.5f,
            center = size.center,
        )
        drawCircle(
            color = crosshairColor.copy(alpha = 0.3f),
            radius = bubbleRadius,
            center = size.center,
        )
        drawLine(
            color = crosshairColor,
            start = Offset(size.center.x - bubbleRadius / 3, size.center.y),
            end = Offset(size.center.x + bubbleRadius / 3, size.center.y),
            strokeWidth = crosshairStroke
        )
        drawLine(
            color = crosshairColor,
            start = Offset(size.center.x, size.center.y - bubbleRadius / 3),
            end = Offset(size.center.x, size.center.y + bubbleRadius / 3),
            strokeWidth = crosshairStroke
        )

        // Bubble
        drawCircle(
            color = primaryColor,
            radius = bubbleRadius,
            center = Offset(bubbleX, bubbleY)
        )
    }
}