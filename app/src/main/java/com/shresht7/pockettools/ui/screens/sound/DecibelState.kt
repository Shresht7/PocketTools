package com.shresht7.pockettools.ui.screens.sound

data class DecibelState(
    val amplitude: Int = 0,
    val waveform: List<Float> = emptyList()
)