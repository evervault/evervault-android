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

    val cageName = "hello-cage"
    val appId = "app-5e6b75800e28"

    var responseText: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val url = "https://$cageName.$appId.cage.evervault.com/compute"
            val pcrClient = OkHttpClient.Builder().build()
            val pcrRequest = Request.Builder()
                .url("https://blackhole.posterior.io/0xljnh")
                .build()

            val jsonPayload = JSONObject()
            jsonPayload.put("hello", "world")

            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                jsonPayload.toString()
            )

            val request = Request.Builder()
                .url(url)
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
