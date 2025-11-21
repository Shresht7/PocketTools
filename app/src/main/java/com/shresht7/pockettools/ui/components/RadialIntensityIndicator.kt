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

/**
 * A Composable function that draws a radial intensity indicator.
 *
 * This indicator consists of concentric circles of dots, where the number and
 * alpha of illuminated dots dynamically change based on the provided `intensity` value.
 * It's suitable for visually representing a scalar intensity or magnitude.
 *
 * @param intensity A float value between 0.0f and 1.0f representing the current intensity level.
 *                  0.0f means no dots are illuminated, 1.0f means all dots are fully illuminated.
 * @param innerRadiusFactor A factor applied to the minimum dimension of the composable's size
 *                          to determine the radius of the innermost ring of dots.
 * @param outerRadiusFactor A factor applied to the minimum dimension of the composable's size
 *                          to determine the radius of the outermost ring of dots.
 * @param center Optional [Offset] to specify the center of the radial indicator.
 *               If null, it defaults to the center of the Canvas.
 * @param steps The number of concentric rings (steps) in the indicator.
 * @param dotsPerCircle The number of dots in each concentric circle.
 * @param color The base color of the dots.
 * @param modifier The modifier for this composable.
 */
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
            // Check if the current intensity is sufficient to illuminate this step/ring.
            // A small offset (0.001f) is added to handle floating-point precision issues
            // when intensity perfectly matches the threshold.
            if (intensity >= threshold - 0.001f) {
                // Calculate the radius for the current ring. It linearly interpolates between
                // innerRadiusFactor and outerRadiusFactor based on the step.
                val radius = buttonRadius + maxRadius * ((i + 1) / steps.toFloat())
                // Calculate the alpha for the dots in this ring. This creates a fading effect
                // as intensity increases, making dots further out brighter.
                val alpha = (intensity - (threshold - 0.2f).coerceIn(0f, 1f))

                for (j in 0 until dotsPerCircle) {
                    // Calculate the angle for each dot in the circle
                    val angle = (2 * Math.PI * j / dotsPerCircle).toFloat()
                    // Calculate the x and y coordinates for each dot
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
