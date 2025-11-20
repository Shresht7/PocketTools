package com.shresht7.pockettools.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

data class Orientation(
    val pitch: Float,
    val roll: Float,
)

@Composable
fun rememberOrientation(): Orientation {
    // Acquire the Sensor Manager
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(SensorManager::class.java) }

    // State Variables
    val acceleration = remember { FloatArray(3) }
    val magnetometer = remember { FloatArray(3) }
    val rotationMatrix = remember { FloatArray(9) }
    val orientation = remember { mutableStateOf(Orientation(0f, 0f)) }

    fun update() {
        if (SensorManager.getRotationMatrix(rotationMatrix, null, acceleration, magnetometer)) {
            val values = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, values)

            // Convert radians -> degrees
            val pitch = Math.toDegrees(values[1].toDouble()).toFloat()
            val roll = Math.toDegrees(values[2].toDouble()).toFloat()

            orientation.value = Orientation(pitch, roll)
        }
    }

    DisposableEffect(Unit) {
        val accelerationListener = object: SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                acceleration[0] = event.values[0]
                acceleration[1] = event.values[1]
                acceleration[2] = event.values[2]
                update()
            }
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }

        sensorManager.registerListener(
            accelerationListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME,
        )

        onDispose {
            sensorManager.unregisterListener(accelerationListener)
        }
    }

    return orientation.value
}