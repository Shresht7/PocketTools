package com.shresht7.pockettools.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.shresht7.pockettools.ui.screens.counter.CounterScreen
import com.shresht7.pockettools.ui.screens.home.HomeScreen
import com.shresht7.pockettools.ui.screens.magnetometer.MagnetometerScreen
import com.shresht7.pockettools.ui.screens.plumbBob.PlumbBobScreen
import com.shresht7.pockettools.ui.screens.ruler.RulerScreen
import com.shresht7.pockettools.ui.screens.sensors.SensorsListScreen
import com.shresht7.pockettools.ui.screens.spiritLevel.SpiritLevelScreen
import com.shresht7.pockettools.ui.screens.tipCalculator.TipCalculatorScreen
import com.shresht7.pockettools.ui.screens.torch.TorchScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    val graph = navController.createGraph(startDestination = Screen.Home) {
        composable<Screen.Home> { HomeScreen(onNavigateToTool = { screen -> navController.navigate(screen) }) }
        composable<Screen.Counter> { CounterScreen(onNavigateUp = { navController.popBackStack() }) }
        composable<Screen.TipCalculator> { TipCalculatorScreen(onNavigateUp = { navController.popBackStack() }) }
        composable<Screen.Torch> { TorchScreen(onNavigateUp = { navController.popBackStack() }) }
        composable<Screen.Ruler> { RulerScreen() }
        composable<Screen.Magnetometer> { MagnetometerScreen(onNavigateUp = { navController.popBackStack() }) }
        composable<Screen.SpiritLevel> { SpiritLevelScreen(onNavigateUp = { navController.popBackStack() }) }
        composable<Screen.PlumbBob> { PlumbBobScreen(onNavigateUp = { navController.popBackStack() }) }
        composable<Screen.SensorsList> { SensorsListScreen(onNavigateUp = { navController.popBackStack() }) }
    }
    NavHost(
        navController = navController,
        graph = graph,
    )
}