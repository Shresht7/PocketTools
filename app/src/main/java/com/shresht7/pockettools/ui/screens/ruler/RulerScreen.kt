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
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val tickColor = MaterialTheme.colorScheme.onBackground
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
        }
    }
}