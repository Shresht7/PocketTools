package com.shresht7.pockettools.ui.screens.barometer

data class BarometerState(
    val pressure: Float = 0f,
    val isAvailable: Boolean = true,
    val waveform: List<Float> = emptyList(),
    val intensity: Float = 0f
)