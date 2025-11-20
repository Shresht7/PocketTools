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

/**
 * Represents the orientation of the device in terms of pitch and roll.
 *
 * @property pitch The rotation around the x-axis, in degrees. Values range from -90 to 90.
 *               When the top of the device tilts towards the user, the pitch is positive.
 * @property roll The rotation around the y-axis, in degrees. Values range from -180 to 180.
 *              When the left side of the device tilts up, the roll is positive.
 */
data class Orientation(
    val pitch: Float,
    val roll: Float,
)

/**
 * A composable function that provides the device's orientation in terms of pitch and roll.
 *
 * This function uses the accelerometer and magnetic field sensors to determine the device's
 * rotation. It registers sensor listeners when the composable enters the composition and
 * unregisters them when it leaves, ensuring efficient resource management.
 *
 * The orientation values are updated in real-time as the device's sensors report new data.
 *
 * @return An [Orientation] data class instance containing the current pitch and roll of the device in degrees.
 */
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

    /** Update the orientation values */
    fun update() {
        // Calculate the rotation matrix
        if (SensorManager.getRotationMatrix(rotationMatrix, null, acceleration, magnetometer)) {
            val values = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, values)

            // Convert radians -> degrees
            val pitch = Math.toDegrees(values[1].toDouble()).toFloat()
            val roll = Math.toDegrees(values[2].toDouble()).toFloat()

            // Update Orientation Values
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

        val magnetometerListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                magnetometer[0] = event.values[0]
                magnetometer[1] = event.values[1]
                magnetometer[2] = event.values[2]
                update()
            }
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }

        // Register the listeners
        sensorManager.registerListener(
            accelerationListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME,
        )
        sensorManager.registerListener(
            magnetometerListener,
            sensorManager.getDefaultSensor((Sensor.TYPE_MAGNETIC_FIELD)),
            SensorManager.SENSOR_DELAY_GAME
        )

        // Unregister the listeners when the composable leaves the composition
        onDispose {
            sensorManager.unregisterListener(accelerationListener)
            sensorManager.unregisterListener(magnetometerListener)
        }
    }

    return orientation.value
}