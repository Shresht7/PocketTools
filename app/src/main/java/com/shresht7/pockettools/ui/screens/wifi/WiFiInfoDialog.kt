package com.shresht7.pockettools.ui.screens.wifi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WiFiInfoDialog(onDismiss: () -> Unit) {
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