package com.shresht7.pockettools.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadialIntensityIndicator(
    intensity: Float,
    innerRadiusFactor: Float,
    outerRadiusFactor: Float,
    center: Offset? = null,
    steps: Int = 5,
    dotsPerCircle: Int = 60,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val center = center ?: Offset(size.width / 2, size.height / 2)
        val buttonRadius = (size.minDimension / 2f) * innerRadiusFactor
        val maxRadius = (size.minDimension / 2f) * outerRadiusFactor

        repeat(steps) { i ->
            val threshold = (i + 1) / steps.toFloat()
            if (intensity >= threshold - 0.001f) {
                val radius = buttonRadius + maxRadius * ((i + 1) / steps.toFloat())
                val alpha = (intensity - (threshold - 0.2f).coerceIn(0f, 1f))

                for (j in 0 until dotsPerCircle) {
                    val angle = (2 * Math.PI * j / dotsPerCircle).toFloat()
                    val x = center.x + radius * cos(angle)
                    val y = center.y + radius * sin(angle)
                    drawCircle(
                        color = color.copy(alpha = alpha),
                        center = Offset(x, y),
                        radius = 5f,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RadialIntensityIndicatorPreview() {
    RadialIntensityIndicator(
        intensity = 0.5f,
        innerRadiusFactor = 0.5f,
        outerRadiusFactor = 0.9f,
        steps = 5,
        dotsPerCircle = 60,
        modifier = Modifier.fillMaxSize(),
    )
}
