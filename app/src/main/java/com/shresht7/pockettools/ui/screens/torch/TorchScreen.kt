package com.shresht7.pockettools.ui.screens.torch

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.shresht7.pockettools.ui.components.RadialIntensityIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TorchScreen(onNavigateUp: () -> Unit = {}) {
    /* The context is used to check and request camera permission */
    val context = LocalContext.current

    /* Whether the camera permission has been granted or not */
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // The Camera Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {granted ->
        hasCameraPermission = granted
    }

    // Ask for permission if not granted
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
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
                title = { Text("Torch") },
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            if (hasCameraPermission) {
                TorchButton(context)
            } else {
                Text(
                    text = "Camera permission required to use torch",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun TorchButton(context: Context) {
    /* Whether the torch is currently turned on */
    var isTorchOn by remember { mutableStateOf(false) }

    /* The torch brightness level */
    var brightness by remember { mutableFloatStateOf(0f) } // 0 = dim, 1 = bright

    // Instantiate the Camera Manager and get the ID of the first camera with flash available
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = remember {
        cameraManager.cameraIdList.firstOrNull { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false
            hasFlash
        }
    }

    // Turn the torch on or off
    LaunchedEffect(isTorchOn) {
        if (cameraId != null) {
            try {
                cameraManager.setTorchMode(cameraId, isTorchOn)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Cleanup the torch when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            if (cameraId != null) {
                try {
                    cameraManager.setTorchMode(cameraId, false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1.0f)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    val oldBrightness = brightness
                    brightness = (brightness - dragAmount / 1000f).coerceIn(0.0f, 1.0f)
                    // Auto-toggle based on brightness crossing thresholds
                    if (oldBrightness > 0f && brightness == 0f) {
                        isTorchOn = false
                    } else if (oldBrightness == 0f && brightness > 0f) {
                        isTorchOn = true
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        // Brightness Indicator Concentric Circles
        RadialIntensityIndicator(
            intensity = brightness,
            innerRadiusFactor = 0.5f,
            outerRadiusFactor = 0.9f,
            steps = 5,
            dotsPerCircle = 60,
            modifier = Modifier.fillMaxSize(),
        )

        // Main Button
        Box(
            modifier = Modifier
                .fillMaxSize(fraction = 0.5f)
                .background(
                    color = if (isTorchOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape,
                )
                .clickable { isTorchOn = !isTorchOn },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (isTorchOn) "ON" else "OFF",
                style = MaterialTheme.typography.headlineLarge,
                color = if (isTorchOn) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TorchScreenPreview() {
    TorchScreen()
}