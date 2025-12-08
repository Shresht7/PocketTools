package com.shresht7.pockettools.ui.screens.wifi

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun WifiStrengthIcon(rssi: Int, modifier: Modifier = Modifier) {
    val color = getStrengthColor(rssi)
    val bars = when {
        rssi >= -55 -> 4
        rssi >= -67 -> 3
        rssi >= -80 -> 2
        else -> 1
    }
    val baseAlpha = 0.3f

    Canvas(modifier = modifier) {
        val strokeWidth = size.width / 12
        val center = Offset(size.width / 2, size.height)

        // Draw base circle
        drawCircle(
            color = color,
            radius = strokeWidth,
            center = center
        )

        // Draw arcs
        for (i in 1..4) {
            val radius = size.width * 0.2f * (i + 1)
            val alpha = if (i <= bars) 1f else baseAlpha
            drawArc(
                color = color.copy(alpha = alpha),
                startAngle = 225f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth)
            )
        }
    }
}