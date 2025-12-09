package com.shresht7.pockettools.ui.screens.wifi

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Normalizes the RSSI (Received Signal Strength Indicator) value to a float between 0.0 and 1.0.
 *
 * This function takes a raw RSSI value (typically in dBm, ranging from -100 to -40 or similar)
 * and maps it to a linear scale from 0.0 (weakest signal) to 1.0 (strongest signal).
 * Values outside the expected range [-100, -40] are coerced to fit within the [0.0, 1.0] bounds.
 *
 * @param rssi The raw RSSI value in dBm.
 * @return A normalized float value between 0.0f and 1.0f, inclusive.
 */
internal fun normalizeRssi(rssi: Int): Float {
    val minRssi = -100
    val maxRssi = -40
    val normalized = (rssi - minRssi).toFloat() / (maxRssi - minRssi).toFloat()
    return normalized.coerceIn(0f, 1f)
}

/**
 * Determines the display color for a Wi-Fi signal based on its RSSI value.
 *
 * This function returns a specific [Color] to visually represent the signal strength:
 * - Green for very strong signals (>= -50 dBm).
 * - Light Green for strong signals (>= -60 dBm).
 * - Orange for fair signals (>= -70 dBm).
 * - Red for weak signals (< -70 dBm).
 *
 * @param rssi The raw RSSI value of the Wi-Fi signal in dBm.
 * @return A [Color] object representing the signal strength.
 */
@Composable
internal fun getStrengthColor(rssi: Int): Color {
    return when {
        rssi >= -50 -> Color(0xFF4CAF50) // Green 500
        rssi >= -60 -> Color(0xFF8BC34A) // Light Green 500
        rssi >= -70 -> Color(0xFFFF9800) // Orange 500
        else -> Color(0xFFF44336)       // Red 500
    }
}
