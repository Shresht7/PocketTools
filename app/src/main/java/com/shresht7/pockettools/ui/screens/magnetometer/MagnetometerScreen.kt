package com.shresht7.pockettools.ui.screens.magnetometer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.shresht7.pockettools.ui.components.RadialIntensityIndicator
import com.shresht7.pockettools.ui.components.WaveformGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagnetometerScreen(
    onNavigateUp: () -> Unit = {},
    viewModel: MagnetometerViewModel = createMagnetometerViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Magnetometer") },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
           MagnetometerUI(viewModel)
        }
    }
}

@Composable
fun MagnetometerUI(
    viewModel: MagnetometerViewModel
) {
    // Collect the state from the ViewModel
    val state by viewModel.state.collectAsState()

    // Start the ViewModel when the composable is first displayed
    LaunchedEffect(Unit) { viewModel.start() }

    // Dispose the ViewModel when the composable is disposed
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize(fraction = 0.8f),
        ) {
            RadialIntensityIndicator(
                intensity = state.intensity,
                innerRadiusFactor = 0.33f,
                outerRadiusFactor = 0.9f,
                steps = 5,
                color = Color.Yellow,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "${state.magnitude.toInt()} μT",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        WaveformGraph(state.waveform, modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true)
@Composable
fun MagnetometerScreenPreview() {
    MagnetometerScreen()
}