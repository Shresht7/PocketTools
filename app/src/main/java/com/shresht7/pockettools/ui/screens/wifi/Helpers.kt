package com.shresht7.pockettools.ui.screens.wifi

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal fun normalizeRssi(rssi: Int): Float {
    val minRssi = -100
    val maxRssi = -40
    val normalized = (rssi - minRssi).toFloat() / (maxRssi - minRssi).toFloat()
    return normalized.coerceIn(0f, 1f)
}

@Composable
internal fun getStrengthColor(rssi: Int): Color {
    return when {
        rssi >= -50 -> Color(0xFF4CAF50) // Green 500
        rssi >= -60 -> Color(0xFF8BC34A) // Light Green 500
        rssi >= -70 -> Color(0xFFFF9800) // Orange 500
        else -> Color(0xFFF44336)       // Red 500
    }
}
