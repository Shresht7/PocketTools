package com.shresht7.pockettools.ui.screens.wifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WiFiViewModel(
    private val context: Context,
    private val wifiManager: WifiManager
) : ViewModel() {

    private val _state = MutableStateFlow(WiFiState())
    val state = _state.asStateFlow()

    private var isReceiverRegistered = false

    private val wifiScanReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val success = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            } else {
                true
            }
            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun scanSuccess() {
        viewModelScope.launch {
            val results = wifiManager.scanResults
            val sortedResults = results.sortedWith(
                compareByDescending<ScanResult> { it.level }
                    .thenBy { it.SSID }
            )
            _state.update {
                val selectedSsid = it.selectedSsid
                val selectedNetwork = sortedResults.find { result -> result.SSID == selectedSsid }
                it.copy(
                    scanResults = sortedResults,
                    signalStrength = selectedNetwork?.level ?: it.signalStrength,
                    isScanning = false
                )
            }
        }
    }

    private fun scanFailure() {
        viewModelScope.launch {
            _state.update { it.copy(scanResults = emptyList(), isScanning = false) }
        }
    }

    fun startListeningForScans() {
        if (!isReceiverRegistered) {
            val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            context.registerReceiver(wifiScanReceiver, intentFilter)
            isReceiverRegistered = true
        }
        triggerScan()
    }

    fun stopListeningForScans() {
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(wifiScanReceiver)
            } catch (e: IllegalArgumentException) {
                // Receiver not registered
            }
            isReceiverRegistered = false
        }
    }

    fun triggerScan() {
        viewModelScope.launch {
            _state.update { it.copy(isScanning = true) }
            if (!wifiManager.startScan()) {
                scanFailure()
            }
        }
    }

    fun selectSsid(ssid: String) {
        viewModelScope.launch {
            _state.update {
                val selectedNetwork = it.scanResults.find { result -> result.SSID == ssid }
                it.copy(
                    selectedSsid = ssid,
                    signalStrength = selectedNetwork?.level ?: 0
                )
            }
        }
    }
}

class WiFiViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return WiFiViewModel(context.applicationContext, wifiManager) as T
    }
}

@Composable
fun createWifiViewModel(): WiFiViewModel {
    val context = LocalContext.current
    return viewModel(factory = WiFiViewModelFactory(context))
}
