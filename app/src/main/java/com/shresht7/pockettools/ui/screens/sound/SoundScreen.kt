package com.shresht7.pockettools.ui.screens.sound

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.shresht7.pockettools.ui.components.RadialIntensityIndicator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun SoundScreen(
    onNavigateUp: () -> Unit = {},
    decibelViewModel: DecibelViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by decibelViewModel.state.collectAsStateWithLifecycle()

    // Permission state for RECORD_AUDIO
    val permissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    )

    // This effect will start/stop recording based on permission status and lifecycle
    DisposableEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            decibelViewModel.startRecording()
        }
        // onDispose is called when the composable leaves the screen
        onDispose { decibelViewModel.stopRecording() }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Decibel Meter") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (permissionState.status.isGranted) {
                DecibelMeterIndicator(amplitude = state.amplitude)
            } else {
                PermissionRationale(
                    showRationale = permissionState.status.shouldShowRationale,
                    onRequestPermission = { permissionState.launchPermissionRequest() },
                    onOpenSettings = { context.openAppSettings() }
                )
            }
        }
    }

    // Trigger the permission request when the screen is first composed
    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
}

@Composable
private fun DecibelMeterIndicator(amplitude: Int) {
    // The amplitude from AudioRecord is Short, max value is ~32767
    // We convert it to a 0f to 1f range for the Radial Indicator
    val intensity = (amplitude / 32767f).coerceIn(0f, 1f)

    RadialIntensityIndicator(
        intensity = intensity,
        innerRadiusFactor = 0.35f,
        outerRadiusFactor = 1.5f,
        steps = 10,
        dotsPerCircle = 60,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}

@Composable
private fun PermissionRationale(
    showRationale: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val text = if (showRationale) {
        "The microphone is important for this feature. Please grant the permission"
    } else {
        "Microphone permission is required to measure sound levels. Please grant the permission in the settings"
    }

    val buttonText = if (showRationale) "Request Permission" else "Open Settings"
    val onClick = if (showRationale) onRequestPermission else onOpenSettings

    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = onClick) {
        Text(buttonText)
    }
}

// Helper function to open app settings
private fun Context.openAppSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    startActivity(intent)
}