package com.evervault.sampleapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.evervault.sdk.cages.AttestationData
import com.evervault.sdk.cages.PCRs
import com.evervault.sdk.cages.trustManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@Composable
fun CageView() {

    val cageName = "hello-cage"
    val appId = "app-000000000000"

    var responseText: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val url = "https://$cageName.$appId.cages.evervault.com/hello"

            val client = OkHttpClient.Builder()
                .trustManager(AttestationData(
                    cageName = cageName,
                    // Replace with legitimate PCR strings when not in debug mode
                    PCRs(
                        pcr0 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                        pcr1 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                        pcr2 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                        pcr8 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                    )
                ))
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response = client.newCall(request).execute()
                val responseMap: Map<String, String> = Gson().fromJson(response.body!!.string(), Map::class.java) as Map<String, String>
                responseText = responseMap["response"]
            } catch (e: IOException) {
                e.printStackTrace()
                responseText = "Error: ${e.message}"
            }
        }
    }

    Text(
        modifier = Modifier.padding(20.dp),
        text = responseText ?: "Loading..."
    )
}
