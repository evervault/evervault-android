package com.evervault.sampleapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.evervault.googlepay.Config
import com.evervault.googlepay.EvervaultPayViewModel
import com.evervault.googlepay.EvervaultPayViewModelFactory
import com.evervault.sampleapplication.navigation.NavigationGraph
import com.evervault.sampleapplication.ui.theme.EvervaultandroidTheme
import com.evervault.sdk.CustomConfig
import com.evervault.sdk.Evervault

class MainActivity : ComponentActivity() {
    private val model: EvervaultPayViewModel by viewModels {
        EvervaultPayViewModelFactory(application, Config(
            appId = "YOUR_EVERVAULT_APP_ID",
            merchantId = "YOUR_EVERVAULT_MERCHANT_ID"
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Evervault.shared.configure(
            teamId = System.getenv("VITE_EV_TEAM_UUID") ?: "",
            appId = System.getenv("VITE_EV_APP_UUID") ?: "",
            customConfig = CustomConfig(isDebugMode = true)
        )
        super.onCreate(savedInstanceState)

        this.model.start()

        setContent {
            var encrypted: String? by remember { mutableStateOf(null) }

            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                encrypted = Evervault.shared.encrypt("Android") as String
            }

            EvervaultandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(navController = navController)
                }
            }
        }
    }
}
