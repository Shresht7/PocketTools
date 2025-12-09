package com.shresht7.pockettools.ui.screens.wifi

import android.net.wifi.ScanResult
import android.os.Build
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


/**
 * Returns a human-readable label for a Wi-Fi signal's strength based on its RSSI value.
 *
 * This function provides a qualitative description of the signal quality:
 * - "Excellent" for very strong signals (>= -50 dBm).
 * - "Good" for strong signals (>= -60 dBm).
 * - "Fair" for fair signals (>= -70 dBm).
 * - "Weak" for weak signals (< -70 dBm).
 *
 * @param rssi The raw RSSI value of the Wi-Fi signal in dBm.
 * @return A string label representing the signal strength.
 */
fun getStrengthLabel(rssi: Int): String {
    return when {
        rssi >= -50 -> "Excellent"
        rssi >= -60 -> "Good"
        rssi >= -70 -> "Fair"
        else -> "Weak"
    }
}



/**
 * Determines the security protocol of a Wi-Fi network from its capabilities string.
 *
 * This function parses the `capabilities` string from a [ScanResult] to identify the highest
 * level of security supported by the network (e.g., WPA3, WPA2, WPA, WEP).
 *
 * @param scanResult The [ScanResult] object for the Wi-Fi network.
 * @return A string representing the security type (e.g., "WPA3", "WPA2/WPA3", "Open").
 */
fun getSecurityType(scanResult: ScanResult): String {
    val capabilities = scanResult.capabilities
    return when {
        capabilities.contains("WPA3") && capabilities.contains("WPA2") -> "WPA2/WPA3"
        capabilities.contains("WPA3") -> "WPA3"
        capabilities.contains("WPA2") -> "WPA2"
        capabilities.contains("WPA") -> "WPA"
        capabilities.contains("WEP") -> "WEP"
        else -> "Open"
    }
}

/**
 * Determines the frequency band of a Wi-Fi network.
 *
 * This function checks the network's frequency and returns a string indicating whether it is
 * operating on the 2.4 GHz, 5 GHz, or 6 GHz band.
 *
 * @param scanResult The [ScanResult] object for the Wi-Fi network.
 * @return A string representing the frequency band (e.g., "2.4 GHz", "5 GHz", "6 GHz").
 */
fun getFrequencyBand(scanResult: ScanResult): String {
    val frequency = scanResult.frequency
    return when {
        frequency in 2400..2500 -> "2.4 GHz"
        frequency in 5100..5900 -> "5 GHz"
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && frequency in 5925..7125 -> "6 GHz"
        else -> "Unknown"
    }
}

/**
 * Calculates the Wi-Fi channel number from its frequency.
 *
 * This function converts the raw frequency (in MHz) from a [ScanResult] into its corresponding
 * channel number for the 2.4 GHz, 5 GHz, and 6 GHz bands.
 *
 * @param scanResult The [ScanResult] object for the Wi-Fi network.
 * @return The channel number as an integer, or `null` if the band is unknown.
 */
fun getChannel(scanResult: ScanResult): Int? {
    val frequency = scanResult.frequency
    return when (getFrequencyBand(scanResult)) {
        "2.4 GHz" -> {
            when (frequency) {
                2484 -> 14 // Channel 14 is a special case for Japan
                else -> (frequency - 2407) / 5
            }
        }
        "5 GHz" -> (frequency - 5000) / 5
        "6 GHz" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) (frequency - 5950) / 5 else null
        else -> null
    }
}