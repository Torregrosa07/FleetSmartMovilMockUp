package com.fleetsmart.mockupsfleetsmartmovil

import android.os.Bundle
import android.preference.PreferenceManager // Importante
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fleetsmart.mockupsfleetsmartmovil.ui.navigation.FleetDriverApp
import com.fleetsmart.mockupsfleetsmartmovil.ui.theme.MockUpsFleetSmartMovilTheme
import org.osmdroid.config.Configuration // Importante

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- CONFIGURACIÓN DEL MAPA AQUÍ (ANTES DE TODO) ---
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