import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.shresht7.pockettools.ui.components.RadialIntensityIndicator
import com.shresht7.pockettools.ui.components.WaveformGraph
import com.shresht7.pockettools.ui.screens.barometer.BarometerViewModel
import com.shresht7.pockettools.ui.screens.barometer.createBarometerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarometerScreen(
    navController: NavController,
    viewModel: BarometerViewModel = createBarometerViewModel()
) {
    val state by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        viewModel.start()
        onDispose {
            viewModel.stop()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Barometer") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isAvailable) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(fraction = 0.8f),
                ) {
                    RadialIntensityIndicator(
                        intensity = state.intensity,
                        innerRadiusFactor = 0.33f,
                        outerRadiusFactor = 0.9f,
                        steps = 5,
                        color = Color.Cyan,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "${state.pressure.toInt()} hPa",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                WaveformGraph(state.waveform, modifier = Modifier.fillMaxSize())
            } else {
                Text(text = "Barometer sensor not available on this device.")
            }
        }
    }
}