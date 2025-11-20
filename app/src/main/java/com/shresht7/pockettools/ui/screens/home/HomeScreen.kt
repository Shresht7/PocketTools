package com.shresht7.pockettools.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.shresht7.pockettools.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    /* The list of tools (in order) to show on the home-page */
    val screens = listOf(
        Screen.Ruler,
        Screen.Torch,
        Screen.Magnetometer,
        Screen.Counter,
        Screen.TipCalculator,
        Screen.SensorsList,
    )

    var query by remember { mutableStateOf("") }
    val filtered = screens.filter { it.title.contains(query, true) }

    var started by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        started = true
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 64.dp, 24.dp, 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("Pocket Tools", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SearchField(query, onQueryChange = { query = it })

            Spacer(modifier = Modifier.height(16.dp))

            ToolBox(filtered, screens, navController, started)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavController(LocalContext.current))
}