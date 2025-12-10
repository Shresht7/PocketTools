package com.shresht7.pockettools.ui.screens.luxmeter

data class LuxMeterState(
    val lux: Float = 0f,
    val isAvailable: Boolean = true,
    val waveform: List<Float> = emptyList(),
    val intensity: Float = 0f
)