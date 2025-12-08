package com.shresht7.pockettools.ui.screens.wifi

data class WiFiState(
    val signalStrength: Int = 0,
    val ssids: List<String> = emptyList(),
    val selectedSsid: String? = null
)
