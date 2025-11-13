package com.shresht7.pockettools.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.shresht7.pockettools.ui.screens.counter.CounterScreen
import com.shresht7.pockettools.ui.screens.home.HomeScreen
import com.shresht7.pockettools.ui.screens.tipCalculator.TipCalculator
import com.shresht7.pockettools.ui.screens.torch.TorchScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    data object Home: Screen("home")
    @Serializable
    data object Counter: Screen("counter")
    @Serializable
    data object TipCalculator: Screen("tipCalculator")
    @Serializable
    data object Torch: Screen("torch")
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val graph = navController.createGraph(startDestination = Screen.Home) {
        composable<Screen.Home> { HomeScreen(navController) }
        composable<Screen.Counter> { CounterScreen(navController) }
        composable<Screen.TipCalculator> { TipCalculator(navController) } // TODO: Rename [TipCalculator] to TipCalculatorScreen to be consistent
        composable<Screen.Torch> { TorchScreen(navController) }
    }
    NavHost(
        navController = navController,
        graph = graph,
    )
}