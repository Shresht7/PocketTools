package com.shresht7.pockettools.ui.screens.ruler

import android.graphics.Paint
import android.util.DisplayMetrics
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@Composable
fun RulerScreen(navController: NavController) {
    /* The context is used to get the display metrics */
    val context = LocalContext.current
    val metrics: DisplayMetrics = context.resources.displayMetrics

    /* Pixels per millimeter */
    val pxPerMm = metrics.ydpi / 25.4f

    // Compute screen height in mm
    val screenHeightPx = metrics.heightPixels.toFloat()
    val screenHeightMm = screenHeightPx / pxPerMm

    Scaffold(
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                val tickColor = MaterialTheme.colorScheme.onBackground
                MetricRuler(screenHeightMm, pxPerMm, tickColor)
                ImperialRuler(screenHeightMm, pxPerMm, tickColor)
            }
        }
    }
}

@Composable
fun MetricRuler(screenHeightMm: Float, pxPerMm: Float, tickColor: Color) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val cmMarkWidth = 75f
        val mmMarkWidth = 25f
        val rulerRight = size.width

        val paint = Paint().apply {
            color = tickColor.toArgb()
            textSize = 32f
            textAlign = Paint.Align.RIGHT
            isAntiAlias = true
        }

        val totalMm = screenHeightMm.toInt()

        for (mm in 0..totalMm) {
            val y = mm * pxPerMm
            when {
                // Centimeter Tick
                mm % 10 == 0 -> {
                    drawLine(
                        color = tickColor,
                        start = Offset(rulerRight - cmMarkWidth, y),
                        end = Offset(rulerRight, y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        "${mm / 10}",
                        rulerRight - cmMarkWidth - 20f,
                        y + paint.textSize / 3,
                        paint
                    )
                }

                // Half Centimeter Ticks
                mm % 5 == 0 -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.85f),
                        start = Offset(rulerRight - (cmMarkWidth / 2), y),
                        end = Offset(rulerRight, y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                }

                // Millimeter Tick
                else -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.7f),
                        start = Offset(rulerRight - mmMarkWidth, y),
                        end = Offset(rulerRight, y),
                        strokeWidth = 2f,
                    )
                }
            }
        }
    }
}

@Composable
fun ImperialRuler(screenHeightMm: Float, pxPerMm: Float, tickColor: Color) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val inchInMm = 25.4f
        val pxPerInch = pxPerMm * inchInMm

        val inchMarkWidth = 80f
        val halfInchMarkWidth = 60f
        val quarterInchMarkWidth = 40f

        val paint = Paint().apply {
            color = tickColor.toArgb()
            textSize = 32f
            textAlign = Paint.Align.RIGHT
            isAntiAlias = true
        }

        val totalInches = (screenHeightMm / inchInMm).toInt() + 1

        // Draw inch subdivisions (0.5 inch steps)
        for (i in 0..(totalInches * 4)) {
            val fraction = i / 4f
            val y = fraction * pxPerInch
            when {
                // Whole Inch
                i % 4 == 0 -> {
                    drawLine(
                        color = tickColor,
                        start = Offset(0f, y),
                        end = Offset(inchMarkWidth, y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        "${fraction.toInt()}",
                        inchMarkWidth + 40f,
                        y + paint.textSize / 3,
                        paint
                    )
                }

                // Half Inch
                i % 2 == 0 -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.75f),
                        start = Offset(0f, y),
                        end = Offset(halfInchMarkWidth, y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                }

                // Quarter Inch
                else -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.5f),
                        start = Offset(0f, y),
                        end = Offset(quarterInchMarkWidth, y),
                        strokeWidth = 2f,
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
    }
}