package com.shresht7.pockettools.ui.screens.ruler

import android.graphics.Paint
import android.util.DisplayMetrics
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlin.math.roundToInt

@Composable
fun RulerScreen(navController: NavController) {
    /* The context is used to get the display metrics */
    val context = LocalContext.current
    val metrics: DisplayMetrics = context.resources.displayMetrics

    /* Pixels per millimeter */
    val pxPerMm = metrics.ydpi / 25.4f

    Scaffold(
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            val tickColor = MaterialTheme.colorScheme.onBackground
            Rulers(pxPerMm, tickColor)
        }
    }
}

@Composable
fun Rulers(pxPerMm: Float, tickColor: Color) {
    val density = LocalDensity.current

    var touchY by remember { mutableStateOf<Float?>(null) }

    // Remember Paint objects so we don't allocate them every frame
    val labelPaint = remember(tickColor) {
        Paint().apply {
            color = tickColor.toArgb()
            textSize = with(density) { 14.dp.toPx() * 1.6f }
            textAlign = Paint.Align.RIGHT
            isAntiAlias = true
        }
    }

    val labelPaintLeft = remember(tickColor) {
        Paint().apply {
            color = tickColor.toArgb()
            textSize = labelPaint.textSize
            textAlign = Paint.Align.LEFT
            isAntiAlias = true
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> touchY = offset.y },
                    onDrag = { change, _ -> touchY = change.position.y },
                    onDragEnd = { /* Keep or Clear */ }
                )
            }
    ) {
        // Dimensions
        val w = size.width
        val h = size.height

        // Padding from edge, if required
//        val horizontalPadding = 24f.dp.toPx()
//        val verticalPadding = 24f.dp.toPx()
        val horizontalPadding = 0f
        val verticalPadding = 0f

        // X Positions for left (imperial) and right (metric) rulers
        val leftX = horizontalPadding
        val rightX = w - horizontalPadding

        // Tick lengths (px)
        val majorTick = 0.11f * w
        val halfMajorTick = 0.075f * w
        val minorTick = 0.04f * w

        // Metric
        val totalMmVisible = ((h - verticalPadding * 2) / pxPerMm).roundToInt()
        for (mm in 0..totalMmVisible) {
            val y = verticalPadding + mm * pxPerMm
            when {
                // Centimeter Tick
                mm % 10 == 0 -> {
                    // Tick Mark
                    drawLine(
                        color = tickColor,
                        start = Offset(rightX - majorTick, y),
                        end = Offset(rightX, y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                    // Label
                    val label = "${mm / 10}"
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        rightX - majorTick - 12f.dp.toPx(),
                        y + labelPaint.textSize / 3,
                        labelPaint
                    )
                }
                // Half-Centimeter Tick
                mm % 5 == 0 -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.85f),
                        start = Offset(rightX - halfMajorTick, y),
                        end = Offset(rightX, y),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round,
                    )
                }
                // Millimeter Ticks
                else -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.7f),
                        start = Offset(rightX - minorTick, y),
                        end = Offset(rightX, y),
                        strokeWidth = 1.4f,
                    )
                }
            }
        }

        // Imperial
        val inchMm = 25.4f
        val pxPerInch = pxPerMm * inchMm
        val totalInchesVisible = ((h - verticalPadding * 2) / pxPerInch).roundToInt()
        for (i in 0..(totalInchesVisible * 4)) {
            // i indexes quarter-inch steps: i % 4 == 0 => whole inch
            val fraction = i / 4f
            val y = verticalPadding + fraction * pxPerInch
            when {
                // 1 Inch
                i % 4 == 0 -> {
                    // Tick Mark
                    drawLine(
                        color = tickColor,
                        start = Offset(leftX, y),
                        end = Offset(leftX + majorTick, y),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round
                    )
                    // Label
                    val label = "${i / 4}"
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        leftX + majorTick + 12f.dp.toPx(),
                        y + labelPaintLeft.textSize / 3,
                        labelPaintLeft
                    )
                }
                // Half-Inch
                i % 2 == 0 -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.85f),
                        start = Offset(leftX, y),
                        end = Offset(leftX + halfMajorTick, y),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round,
                    )
                }
                // Quarter-Inch Minor
                else -> {
                    drawLine(
                        color = tickColor.copy(alpha = 0.65f),
                        start = Offset(leftX, y),
                        end = Offset(leftX + minorTick, y),
                        strokeWidth = 1.4f,
                    )
                }
            }
        }

        // Measurement Mark
        if (touchY != null) {
            val snappedMm = (touchY!! / pxPerMm)
            val snappedIn = (touchY!! / (pxPerMm * 25.4f))
            val snappedY = snappedMm * pxPerMm
            // Draw Red Line
            drawLine(
                color = Color.Red,
                start = Offset(0f, snappedY),
                end = Offset(size.width, snappedY),
                strokeWidth = 2.dp.toPx()
            )
            // Inch Mark
            val snappedInText = String.format("%.1f", snappedIn)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    snappedInText,
                    128.dp.toPx(),
                    snappedY - 16.dp.toPx(),
                    labelPaint
                )
            }
            // Centimeter Mark
            val snappedCmText = String.format("%.1f", snappedMm / 10)
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    snappedCmText,
                    size.width - 128.dp.toPx(),
                    snappedY - 16.dp.toPx(),
                    labelPaint
                )
            }
        }
    }
}