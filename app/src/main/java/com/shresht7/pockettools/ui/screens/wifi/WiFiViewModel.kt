package com.shresht7.pockettools.ui.screens.wifi

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WiFiViewModel(
    private val wifiManager: WifiManager
) : ViewModel() {

    private val _state = MutableStateFlow(WiFiState())
    val state = _state.asStateFlow()

    fun startScan() {
        // TODO: Implement WiFi scanning
    }

    fun stopScan() {
        // TODO: Implement stopping WiFi scanning
    }
}

class WiFiViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return WiFiViewModel(wifiManager) as T
    }
}

@Composable
fun createWifiViewModel(): WiFiViewModel {
    val context = LocalContext.current
    return viewModel(factory = WiFiViewModelFactory(context))
}
