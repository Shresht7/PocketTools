package com.shresht7.pockettools.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Straighten
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String, val title: String) {

    @Serializable
    data object Home: Screen("home", "Home")
    @Serializable
    data object Counter: Screen("counter", "Counter")
    @Serializable
    data object TipCalculator: Screen("tipCalculator", "Tip Calculator")
    @Serializable
    data object Torch: Screen("torch", "Torch")
    @Serializable
    data object Ruler: Screen("ruler", "Ruler")
    @Serializable
    data object SensorsList: Screen("sensorsList", "Sensors List")
    @Serializable
    data object Magnetometer: Screen("magnetometer", "Magnetometer")

    val Screen.icon: ImageVector
        get() = when (this) {
            Home -> Icons.Default.Home
            Counter -> Icons.Outlined.Numbers
            Magnetometer -> Icons.Outlined.CompassCalibration
            Ruler -> Icons.Outlined.Straighten
            SensorsList -> Icons.Outlined.Sensors
            TipCalculator -> Icons.Outlined.Payment
            Torch -> Icons.Outlined.FlashlightOn
        }
}

