package com.shresht7.pockettools.ui.screens.magnetometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MagnetometerViewModel(
    private val sensorManager: SensorManager
): ViewModel(), SensorEventListener {

    /* The underlying [MagnetometerState] */
    private val _state = MutableStateFlow(MagnetometerState())
    val state = _state.asStateFlow()

    /* Low Pass Alpha Filter */
    private val lowPassAlpha = 0.1f

    /* Smoothed sensor reading value */
    private var smoothed = 0f

    /* Event listener that triggers whenever the sensor value changes */
    override fun onSensorChanged(event: SensorEvent) {

        // Retrieve the sensor readings
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Calculate the magnitude of the vector
        val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)

        // Smooth the output using the lowPassAlpha
        smoothed = (smoothed * (1 - lowPassAlpha)) + (magnitude * lowPassAlpha)

        // Update the state
        val currentState = _state.value
        val intensity = ((smoothed - 25) * currentState.sensitivity) / 50
        val updatedWaveform = currentState.waveform.toMutableList().apply {
            add(smoothed)
            if (size > 150) {
                removeFirst()
            }
        }.let { ArrayDeque(it) }

        _state.value = currentState.copy(
            magnitude = smoothed,
            waveform = updatedWaveform,
            intensity = intensity
        )
    }

    // Implemented to satisfy the SensorEventListener interface
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    /* Recalibrate the sensor */
    fun calibrate() {
        _state.value = _state.value.copy(baseline = smoothed)
    }

    /* Adjust the sensitivity level */
    fun setSensitivity(value: Float) {
        _state.value = _state.value.copy(sensitivity = value)
    }

    /* Register the sensor listener */
    fun start() {
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    /* Unregister the sensor listener */
    fun stop() {
        sensorManager.unregisterListener(this)
    }

}

class MagnetometerVMFactory(
    private val context: Context
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return MagnetometerViewModel(sensorManager) as T
    }
}

@Composable
fun createMagnetometerViewModel(): MagnetometerViewModel {
    val context = LocalContext.current
    return viewModel(factory = MagnetometerVMFactory(context))
}