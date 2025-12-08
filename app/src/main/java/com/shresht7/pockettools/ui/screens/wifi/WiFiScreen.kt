package com.shresht7.pockettools.ui.screens.wifi

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.shresht7.pockettools.ui.components.RadialIntensityIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiFiScreen(
    onNavigateUp: () -> Unit = {},
    viewModel: WiFiViewModel = createWifiViewModel()
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    
    DisposableEffect(hasPermission) {
        if (hasPermission) {
            viewModel.startListeningForScans()
        }
        onDispose {
            if (hasPermission) {
                viewModel.stopListeningForScans()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("WiFi Strength") },
                actions = {
                    if (hasPermission) {
                        IconButton(onClick = { viewModel.triggerScan() }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (hasPermission) {
                WifiStrengthContent(viewModel)
            } else {
                Text("Location permission is required to scan for WiFi networks.")
            }
        }
    }
}

private fun normalizeRssi(rssi: Int): Float {
    val minRssi = -100
    val maxRssi = -40
    val normalized = (rssi - minRssi).toFloat() / (maxRssi - minRssi).toFloat()
    return normalized.coerceIn(0f, 1f)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiStrengthContent(viewModel: WiFiViewModel) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.4f),
                contentAlignment = Alignment.Center
            ) {
                RadialIntensityIndicator(
                    intensity = normalizeRssi(state.signalStrength),
                    innerRadiusFactor = 0.33f,
                    outerRadiusFactor = 0.9f,
                    steps = 10,
                    dotsPerCircle = 60,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = if (state.selectedSsid != null) "${state.signalStrength} dBm" else "-- dBm",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.scanResults, key = { it.BSSID }) { result ->
                    if (result.SSID.isNotBlank()) {
                        Card(
                            onClick = { viewModel.selectSsid(result.SSID) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = if (result.SSID == state.selectedSsid) {
                                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                            } else {
                                CardDefaults.cardColors()
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = result.SSID,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${result.level} dBm",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.isScanning) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxSize()
            ) {}
            CircularProgressIndicator()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WifiScreenPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("WiFi Screen Preview")
    }
}