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

    val enclaveName = BuildConfig.ENCLAVE_NAME
    val appUuid = BuildConfig.APP_UUID

    var cachedCallResponseText: String? by remember { mutableStateOf(null) }
    var staticPCRCallResponseText: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            staticPCRCallResponseText = staticPCRsEnclaveRequest(enclaveName, appUuid)
            cachedCallResponseText = cacheManagerEnclaveCall(enclaveName, appUuid)
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

// Data type to mirror the Evervault API response shape from the Enclave attestation info endpoint.
data class PCRContainer(
    val data: List<PCRs>
)

fun cacheManagerEnclaveCall(enclaveName: String, appUuid: String): String {
    val url = "https://$enclaveName.$appUuid.enclave.evervault.com/compute"
    val pcrClient = OkHttpClient.Builder().build()
    val pcrRequest = Request.Builder()
        .url(BuildConfig.PCR_CALLBACK_URL)
        .header("x-evervault-app-id", appUuid.replace("-","_"))
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
            val type = object : TypeToken<PCRContainer>() {}.type
            val responseMap: PCRContainer = Gson().fromJson(pcrResponse.body!!.string(), type)
            responseMap.data
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
    val url = "https://$enclaveName.$appUuid.enclave.evervault.com/compute"

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
                    pcr0 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    pcr1 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    pcr2 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    pcr8 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
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
