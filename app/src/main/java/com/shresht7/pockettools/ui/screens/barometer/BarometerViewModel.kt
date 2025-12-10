package com.shresht7.pockettools.ui.screens.barometer

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

class BarometerViewModel(
    private val sensorManager: SensorManager
) : ViewModel(), SensorEventListener {

    private val _state = MutableStateFlow(BarometerState())
    val state: StateFlow<BarometerState> = _state

    private val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    init {
        if (pressureSensor == null) {
            _state.value = _state.value.copy(isAvailable = false)
        }
    }

    fun start() {
        pressureSensor?.let {
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
        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            _state.value = _state.value.copy(pressure = event.values[0])
        }
    }
}

class BarometerViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarometerViewModel::class.java)) {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            @Suppress("UNCHECKED_CAST")
            return BarometerViewModel(sensorManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun createBarometerViewModel(): BarometerViewModel {
    val context = LocalContext.current
    return viewModel(factory = BarometerViewModelFactory(context.applicationContext))
}
