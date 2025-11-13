package com.shresht7.pockettools.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shresht7.pockettools.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pocket Tools") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { navController.navigate(Screen.Counter) }) {
                Text("Counter")
            }
            Button(onClick = { navController.navigate(Screen.TipCalculator) }) {
                Text("Tip Calculator")
            }
            Button(onClick = { navController.navigate(Screen.Torch) }) {
                Text("Torch")
            }
            Button(onClick = { navController.navigate(Screen.Ruler) }) {
                Text("Ruler")
            }
            Button(onClick = { navController.navigate(Screen.SensorsList) }) {
                Text("SensorsList")
            }
        }
    }
}