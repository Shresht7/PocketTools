package com.shresht7.pockettools.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.shresht7.pockettools.ui.screens.counter.CounterScreen
import com.shresht7.pockettools.ui.screens.home.HomeScreen
import com.shresht7.pockettools.ui.screens.magnetometer.MagnetometerScreen
import com.shresht7.pockettools.ui.screens.ruler.RulerScreen
import com.shresht7.pockettools.ui.screens.sensors.SensorsListScreen
import com.shresht7.pockettools.ui.screens.tipCalculator.TipCalculatorScreen
import com.shresht7.pockettools.ui.screens.torch.TorchScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    val graph = navController.createGraph(startDestination = Screen.Home) {
        composable<Screen.Home> { HomeScreen(navController) }
        composable<Screen.Counter> { CounterScreen(navController) }
        composable<Screen.TipCalculator> { TipCalculatorScreen(navController) }
        composable<Screen.Torch> { TorchScreen(navController) }
        composable<Screen.Ruler> { RulerScreen(navController) }
        composable<Screen.SensorsList> { SensorsListScreen(navController) }
        composable<Screen.Magnetometer> { MagnetometerScreen(navController) }
    }
    NavHost(
        navController = navController,
        graph = graph,
    )
}