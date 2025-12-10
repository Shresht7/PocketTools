package com.shresht7.pockettools.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.FlashlightOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Speaker
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * A sealed class representing the screens in the application.
 *
 * Each screen is a singleton object that contains its route, title, and icon.
 * This provides a type-safe way to manage navigation and screen metadata.
 *
 * @property route The navigation route for the screen.
 * @property title The title of the screen, displayed in the UI.
 * @property icon The icon representing the screen. This property is ignored during serialization.
 */
@Serializable
sealed class Screen(
    val route: String,
    val title: String,
    @Transient val icon: ImageVector = Icons.Default.Home,
) {
    @Serializable
    data object Home : Screen("home", "Home", Icons.Default.Home)

    @Serializable
    data object Counter : Screen("counter", "Counter", Icons.Outlined.Numbers)

    @Serializable
    data object TipCalculator : Screen("tipCalculator", "Tip Calculator", Icons.Outlined.Payment)

    @Serializable
    data object Torch : Screen("torch", "Torch", Icons.Outlined.FlashlightOn)

    @Serializable
    data object Ruler : Screen("ruler", "Ruler", Icons.Outlined.Straighten)

    @Serializable
    data object SensorsList : Screen("sensorsList", "Sensors List", Icons.Outlined.Sensors)

    @Serializable
    data object Magnetometer :
        Screen("magnetometer", "Magnetometer", Icons.Outlined.CompassCalibration)

    @Serializable
    data object SpiritLevel : Screen("spirit-level", "Spirit Level", Icons.Outlined.Balance)

    @Serializable
    data object PlumbBob : Screen("plumb-bob", "Plumb Bob", Icons.Outlined.Circle)

    @Serializable
    data object WiFi : Screen("wifi", "WiFi", Icons.Outlined.Wifi)

    @Serializable
    data object Sound : Screen("sound", "Sound", Icons.Outlined.Speaker)

    @Serializable
    data object Geolocation : Screen("geolocation", "Geolocation", Icons.Outlined.MyLocation)
}

