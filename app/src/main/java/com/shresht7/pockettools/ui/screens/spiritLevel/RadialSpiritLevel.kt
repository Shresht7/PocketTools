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
import com.shresht7.pockettools.sensor.Orientation

/**
 * A composable that displays a radial (bull's eye) spirit level.
 *
 * @param orientation Contains the pitch and roll data.
 * @param primaryColor The primary color used for accents.
 * @param crosshairColor The color of the central crosshair.
 * @param bubbleRadiusDp The radius of the main bubble.
 * @param bubbleColor The color of the main bubble.
 * @param borderCircleColor The color of the outer border circle.
 * @param borderCircleStrokeWidthDp The stroke width of the outer border.
 */
@Composable
fun SpiritLevel(
    orientation: Orientation,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    crosshairColor: Color = MaterialTheme.colorScheme.onSurface,
    crosshairStrokeDp: Dp = 2.dp,
    bubbleRadiusDp: Dp = 25.dp,
    bubbleColor: Color = MaterialTheme.colorScheme.onSurface,
    borderCircleColor: Color = MaterialTheme.colorScheme.primary,
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

        val bubbleX = size.center.x + mapTiltToOffset(orientation.roll, maxOffset)
        val bubbleY = size.center.y + mapTiltToOffset(orientation.pitch, maxOffset)

        val isLevel = orientation.roll in -2f..2f && orientation.pitch in -2f..2f

        // Background Circle
        drawCircle(
            color = borderCircleColor.copy(alpha = if (isLevel) 0.7f else 0.3f),
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
            color = primaryColor.copy(alpha = if (isLevel) 0.5f else 0.3f),
            radius = maxOffset,
            center = Offset(bubbleX, bubbleY)
        )
        drawCircle(
            color = bubbleColor,
            radius = bubbleRadius,
            center = Offset(bubbleX, bubbleY)
        )
    }
}