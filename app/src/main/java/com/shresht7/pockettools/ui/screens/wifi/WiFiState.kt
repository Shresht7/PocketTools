package com.shresht7.pockettools.ui.screens.wifi

import android.net.wifi.ScanResult

data class WiFiState(
    val scanResults: List<ScanResult> = emptyList(),
    val selectedSsid: String? = null,
    val signalStrength: Int = 0, // This will be RSSI
    val isScanning: Boolean = false
)
