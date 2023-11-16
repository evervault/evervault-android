package com.evervault.sampleapplication.ui.views

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
import com.evervault.sdk.cages.PCRCallbackError
import com.evervault.sdk.cages.PCRs
import com.evervault.sdk.cages.PcrCallback
import com.evervault.sdk.cages.cagesTrustManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

@Composable
fun CageView() {

    val cageName = "staging-synthetic-cage"
    val appId = "app-1bba8ba15402"

    var responseText: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val url = "https://$cageName.$appId.cage.evervault.dev/echo"
            val pcrClient = OkHttpClient.Builder().build()
            val pcrRequest = Request.Builder()
                .url("https://blackhole.posterior.io/0xljnh")
                .build()

            // Create JSON using a JSONObject or a string
            val jsonPayload = JSONObject()
            jsonPayload.put("hello", "world")
            // Or if you prefer a raw string: val jsonPayload = """{"hello":"world"}"""

            // Create RequestBody with JSON media type
            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                jsonPayload.toString()
            )

            val request = Request.Builder()
                .url(url)
                .header("api-key", "ev:key:1:2KYxc0TJZw6ozOFR7IxDX6PdCA6HdpvVge5NiRwsT00QCYvxlhjmMk786ywrSNUIO:MD23Ke:lfTpz5")
                .header("content-type", "application/json")
                .post(requestBody)
                .build()

            try {
                val pcrCallback: PcrCallback = {
                    val pcrResponse = pcrClient.newCall(pcrRequest).execute()
                    val type = object : TypeToken<List<PCRs>>() {}.type
                    val responseMap: List<PCRs> = Gson().fromJson(pcrResponse.body!!.string(), type)
                    responseMap
                }

                val client = OkHttpClient.Builder()
                    .cagesTrustManager(
                        AttestationData(
                            cageName = cageName,
                            pcrCallback
                        ),
                        appId
                    )
                    .build()

                val response = client.newCall(request).execute()
                val responseMap: Map<String, String> = Gson().fromJson(response.body!!.string(), Map::class.java) as Map<String, String>
                println(responseMap["body"])
                responseText = responseMap["body"].toString()
            } catch (e: Exception) {
                when(e) {
                    is IOException, is PCRCallbackError -> {
                        e.printStackTrace()
                        responseText = "Error: ${e.message}"
                    }
                    else -> throw e
                }
            }
        }
    }

    Text(
        modifier = Modifier.padding(20.dp),
        text = responseText ?: "Loading..."
    )
}
