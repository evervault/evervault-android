package com.evervault.sampleapplication.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.evervault.sdk.enclaves.AttestationData
import com.evervault.sdk.enclaves.PCRCallbackError
import com.evervault.sdk.enclaves.PCRs
import com.evervault.sdk.enclaves.PcrCallback
import com.evervault.sdk.enclaves.enclavesTrustManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.evervault.sampleapplication.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

@Composable
fun EnclaveView() {

    val enclaveName = BuildConfig.ENCLAVE_UUID
    val appUuid = BuildConfig.APP_UUID

    var cachedCallResponseText: String? by remember { mutableStateOf(null) }
    var staticPCRCallResponseText: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            cachedCallResponseText = staticPCRsEnclaveRequest(enclaveName, appUuid)
            staticPCRCallResponseText = staticPCRsEnclaveRequest(enclaveName, appUuid)
        }
    }

    Column {
        Row {
            Text(
                modifier = Modifier.padding(20.dp),
                text = cachedCallResponseText ?: "Loading result with PCR callback"
            )
        }
        Row {
            Text(
                modifier = Modifier.padding(20.dp),
                text = staticPCRCallResponseText ?: "Loading result with static PCRs"
            )
        }
    }
}

fun cacheManagerEnclaveCall(enclaveName: String, appUuid: String): String {
    val url = "https://$enclaveName.$appUuid.cage.evervault.com/compute"
    val pcrClient = OkHttpClient.Builder().build()
    val pcrRequest = Request.Builder()
        .url(BuildConfig.PCR_CALLBACK_URL)
        .build()

    val jsonPayload = JSONObject()
    jsonPayload.put("a", 1)
    jsonPayload.put("b", 2)

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
            .enclavesTrustManager(
                AttestationData(
                    enclaveName = enclaveName,
                    pcrCallback
                ),
                appUuid
            )
            .build()

        val response = client.newCall(request).execute()
        val responseMap: Map<String, String> = Gson().fromJson(response.body!!.string(), Map::class.java) as Map<String, String>
        return responseMap["sum"].toString()
    } catch (e: Exception) {
        when(e) {
            is IOException, is PCRCallbackError -> {
                e.printStackTrace()
                return "Error: ${e.message}"
            }
            else -> throw e
        }
    }
}

fun staticPCRsEnclaveRequest(enclaveName: String, appUuid: String): String {
    val url = "https://$enclaveName.$appUuid.cage.evervault.com/compute"

    val jsonPayload = JSONObject()
    jsonPayload.put("a", 1)
    jsonPayload.put("b", 2)

    val requestBody = RequestBody.create(
        "application/json; charset=utf-8".toMediaTypeOrNull(),
        jsonPayload.toString()
    )

    val client = OkHttpClient.Builder()
        .enclavesTrustManager(
            AttestationData(
                enclaveName = enclaveName,
                // Replace with legitimate PCR strings when not in debug mode
                PCRs(
                    pcr0 = "00afa537918248861eccd4640398d866487bef4a94dfe44e79f62fd6ae56d720210be61dc1e4d10a63cfa7568efbe18e",
                    pcr1 = "bcdf05fefccaa8e55bf2c8d6dee9e79bbff31e34bf28a99aa19e6b29c37ee80b214a414b7607236edf26fcb78654e63f",
                    pcr2 = "de1af81e4c35a9f63dbc0d518f71197c42e8bbfef299e1a6f282d8ecd430e3b97f78bd6481d3c08d8131278403503e82",
                    pcr8 = "1a65e2ed7be8b98f135187708038e36a019a86863f072cd27e0d57cbc244819c86047c151e403b69be6647d21a06ede0"
                )
            ),
            appUuid
        )
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    return try {
        val response = client.newCall(request).execute()
        val responseMap: Map<String, String> = Gson().fromJson(response.body!!.string(), Map::class.java) as Map<String, String>
        responseMap["sum"].toString()
    } catch (e: IOException) {
        e.printStackTrace()
        "Error: ${e.message}"
    }
}
