package com.shresht7.pockettools.ui.screens.magnetometer

data class MagnetometerState(
    val magnitude: Float = 0f,
    val baseline: Float = 0f,
    val sensitivity: Float = 1f,    // 1x = Normal
    val waveform: ArrayDeque<Float> = ArrayDeque(),
    val intensity: Float = 0f
)