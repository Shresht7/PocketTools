package com.shresht7.pockettools.ui.screens.wifi

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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

@Composable
private fun getStrengthColor(rssi: Int): Color {
    return when {
        rssi >= -50 -> Color(0xFF4CAF50) // Green 500
        rssi >= -60 -> Color(0xFF8BC34A) // Light Green 500
        rssi >= -70 -> Color(0xFFFF9800) // Orange 500
        else -> Color(0xFFF44336)       // Red 500
    }
}

@Composable
fun StrengthBar(
    strength: Float, // Normalized 0.0f to 1.0f
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val barWidth = size.width * strength
        drawRect(
            color = color.copy(alpha = 0.3f),
        )
        drawRect(
            color = color,
            size = size.copy(width = barWidth)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiStrengthContent(viewModel: WiFiViewModel) {
    val state by viewModel.state.collectAsState()

    val animatedIntensity by animateFloatAsState(
        targetValue = normalizeRssi(state.signalStrength),
        label = "intensityAnimation",
        animationSpec = tween(durationMillis = 500)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulsingIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        ),
        label = "pulseAnimation"
    )

    val listAlpha = if (state.isScanning) 0.6f else 1.0f

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            RadialIntensityIndicator(
                intensity = if (state.isScanning) pulsingIntensity else animatedIntensity,
                innerRadiusFactor = 0.33f,
                outerRadiusFactor = 0.9f,
                steps = 10,
                dotsPerCircle = 60,
                color = if (state.isScanning) Color.Gray else getStrengthColor(state.signalStrength),
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = if (state.selectedSsid != null && !state.isScanning) "${state.signalStrength} dBm" else if (state.isScanning) "Scanning..." else "-- dBm",
                style = MaterialTheme.typography.headlineLarge,
                color = if (state.isScanning) Color.Gray else getStrengthColor(state.signalStrength)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)
                .alpha(listAlpha),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.scanResults, key = { it.BSSID }) { result ->
                if (result.SSID.isNotBlank()) {
                    val itemStrengthColor = getStrengthColor(result.level)
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
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StrengthBar(
                                strength = normalizeRssi(result.level),
                                color = itemStrengthColor,
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(8.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${result.level} dBm",
                                style = MaterialTheme.typography.bodyLarge,
                                color = itemStrengthColor,
                                modifier = Modifier.width(60.dp)
                            )
                        }
                    }
                }
            }
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
