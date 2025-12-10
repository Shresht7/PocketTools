package com.shresht7.pockettools.navigation

import BarometerScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.shresht7.pockettools.ui.screens.counter.CounterScreen
import com.shresht7.pockettools.ui.screens.geolocation.GeolocationScreen
import com.shresht7.pockettools.ui.screens.home.HomeScreen
import com.shresht7.pockettools.ui.screens.luxmeter.LuxMeterScreen
import com.shresht7.pockettools.ui.screens.magnetometer.MagnetometerScreen
import com.shresht7.pockettools.ui.screens.plumbBob.PlumbBobScreen
import com.shresht7.pockettools.ui.screens.ruler.RulerScreen
import com.shresht7.pockettools.ui.screens.sensors.SensorsListScreen
import com.shresht7.pockettools.ui.screens.sound.SoundScreen
import com.shresht7.pockettools.ui.screens.spiritLevel.SpiritLevelScreen
import com.shresht7.pockettools.ui.screens.tipCalculator.TipCalculatorScreen
import com.shresht7.pockettools.ui.screens.torch.TorchScreen
import com.shresht7.pockettools.ui.screens.wifi.WiFiScreen

/**
 * Defines the navigation graph for the application.
 *
 * This composable sets up the routes to all the different screens (tools) in the app.
 * It uses a type-safe navigation pattern with a `NavHost` and a separately created graph.
 *
 * The `AppNavHost` defines two reusable callbacks:
 * - `onNavigateUp`: A simple lambda that pops the back stack, used by tool screens to navigate back.
 * - `onNavigateToTool`: A callback passed to the [HomeScreen] to navigate to a selected tool.
 *
 * @param navController The [NavHostController] that manages the navigation state and allows
 *                      for navigating between screens.
 */
@Composable
fun AppNavHost(navController: NavHostController) {
    val onNavigateUp: () -> Unit = { navController.popBackStack() }
    val onNavigateToTool: (Screen) -> Unit = { screen -> navController.navigate(screen) }
    val screens = listOf(
        Screen.Ruler,
        Screen.Torch,
        Screen.WiFi,
        Screen.Magnetometer,
        Screen.Sound,
        Screen.SpiritLevel,
        Screen.PlumbBob,
        Screen.Counter,
        Screen.TipCalculator,
        Screen.SensorsList,
        Screen.Geolocation,
        Screen.Barometer,
        Screen.LuxMeter,
    )

    val graph = navController.createGraph(startDestination = Screen.Home) {
        composable<Screen.Home> {
            HomeScreen(
                screens = screens,
                onNavigateToTool = onNavigateToTool,
            )
        }
        composable<Screen.Counter> { CounterScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.TipCalculator> { TipCalculatorScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.Torch> { TorchScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.Ruler> { RulerScreen() }
        composable<Screen.Magnetometer> { MagnetometerScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.SpiritLevel> { SpiritLevelScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.PlumbBob> { PlumbBobScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.SensorsList> { SensorsListScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.WiFi> { WiFiScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.Sound> { SoundScreen(onNavigateUp = onNavigateUp) }
        composable<Screen.Geolocation> { GeolocationScreen(navController = navController) }
        composable<Screen.Barometer> { BarometerScreen(navController = navController) }
        composable<Screen.LuxMeter> { LuxMeterScreen(navController = navController) }
    }
    NavHost(
        navController = navController,
        graph = graph,
    )
}