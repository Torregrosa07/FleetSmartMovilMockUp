package com.fleetsmart.mockupsfleetsmartmovil

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fleetsmart.mockupsfleetsmartmovil.ui.navigation.FleetDriverApp
import com.fleetsmart.mockupsfleetsmartmovil.ui.theme.MockUpsFleetSmartMovilTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- CORRECCIÓN MAPA: Configuración inicial aquí ---
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        Configuration.getInstance().userAgentValue = packageName
        // ----------------------------------------------------

        enableEdgeToEdge()

        setContent {
            MockUpsFleetSmartMovilTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FleetDriverApp()
                }
            }
        }
    }
}