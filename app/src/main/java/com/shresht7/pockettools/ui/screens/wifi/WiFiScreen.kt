package com.shresht7.pockettools.ui.screens.wifi

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

    var showInfoDialog by remember { mutableStateOf(false) }

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

    if (showInfoDialog) {
        DbmInfoDialog(onDismiss = { showInfoDialog = false })
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
                    IconButton(onClick = { showInfoDialog = true }) {
                        Icon(Icons.Filled.Info, contentDescription = "Information")
                    }
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
                WiFiStrength(viewModel)
            } else {
                Text("Location permission is required to scan for WiFi networks.")
            }
        }
    }
}

@Composable
fun DbmInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Wi-Fi Information") },
        text = {
            Column {
                Text("Understanding Wi-Fi Signals:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("• dBm (decibel-milliwatts): Measures signal power. Lower (more negative) values mean weaker signals.")
                Text("  • -30 to -50 dBm: Excellent")
                Text("  • -50 to -60 dBm: Good")
                Text("  • -60 to -70 dBm: Fair")
                Text("  • Below -70 dBm: Weak")
                Spacer(modifier = Modifier.height(16.dp))

                Text("Frequency Bands:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("• 2.4 GHz: Wider range, better penetration through walls, but slower speeds and more interference.")
                Text("• 5 GHz: Faster speeds, less interference, but shorter range and struggles with obstacles.")
                Text("• 6 GHz: (Wi-Fi 6E) Even faster, more channels, very low interference, shortest range. Requires compatible hardware.")
                Spacer(modifier = Modifier.height(16.dp))

                Text("Security Types:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Open: No security, data is unencrypted.")
                Text("• WEP: Very old, easily cracked, should be avoided.")
                Text("• WPA/WPA2: Common, offers good security. WPA2 is stronger.")
                Text("• WPA3: Latest and strongest security standard.")
                Spacer(modifier = Modifier.height(16.dp))

                Text("Channels:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Wi-Fi operates on different channels within each frequency band. Overlapping channels can cause interference and slow down your connection.")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiFiStrength(viewModel: WiFiViewModel) {
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
                innerRadiusFactor = 0.35f,
                outerRadiusFactor = 1.5f,
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
                    WifiInfoCard(
                        scanResult = result,
                        isSelected = result.SSID == state.selectedSsid,
                        onClick = { viewModel.selectSsid(result.SSID) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiInfoCard(
    scanResult: ScanResult,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val itemStrengthColor = getStrengthColor(scanResult.level)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scanResult.SSID,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = scanResult.BSSID,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Security: ${getSecurityType(scanResult)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Band: ${getFrequencyBand(scanResult)}, Channel: ${getChannel(scanResult) ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = getStrengthLabel(scanResult.level),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = itemStrengthColor
                )
                Text(
                    text = "${scanResult.level} dBm",
                    style = MaterialTheme.typography.bodySmall,
                    color = itemStrengthColor,
                )
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
