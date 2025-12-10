package com.shresht7.pockettools.ui.screens.luxmeter

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
import kotlinx.coroutines.flow.StateFlow

class LuxMeterViewModel(
    private val sensorManager: SensorManager
) : ViewModel(), SensorEventListener {

    private val _state = MutableStateFlow(LuxMeterState())
    val state: StateFlow<LuxMeterState> = _state

    private val lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    init {
        if (lightSensor == null) {
            _state.value = _state.value.copy(isAvailable = false)
        }
    }

    fun start() {
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]
            val intensity = (lux / 120f).coerceIn(0f, 1f)
            val newWaveform = (_state.value.waveform + lux).takeLast(100)
            _state.value = _state.value.copy(
                lux = lux,
                intensity = intensity,
                waveform = newWaveform
            )
        }
    }
}

class LuxMeterViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LuxMeterViewModel::class.java)) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            @Suppress("UNCHECKED_CAST")
            return LuxMeterViewModel(sensorManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun createLuxMeterViewModel(): LuxMeterViewModel {
    val context = LocalContext.current
    return viewModel(factory = LuxMeterViewModelFactory(context.applicationContext))
}
