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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.evervault.sdk.enclaves.AttestationData
import com.evervault.sdk.enclaves.PCRs
import com.evervault.sdk.enclaves.PcrCallback
import com.evervault.sdk.enclaves.enclavesTrustManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.evervault.sampleapplication.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EnclaveView() {
    var cachedCallResponseText: String? by remember { mutableStateOf(null) }
    var staticPCRCallResponseText: String? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            staticPCRCallResponseText = EvervaultHttpClient.staticPCRsEnclaveRequest()
            cachedCallResponseText = EvervaultHttpClient.cachedPCRsEnclaveRequest()
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
                modifier = Modifier.padding(20.dp)
                    .semantics { testTagsAsResourceId = true }
                    .testTag("Enclave Response"),
                text = staticPCRCallResponseText ?: "Loading result with static PCRs"
            )
        }
    }
}

object EvervaultHttpClient {
    private var client: OkHttpClient? = null
    private const val enclaveURL = BuildConfig.ENCLAVE_URL
    private const val enclaveName = BuildConfig.ENCLAVE_UUID
    private const val appUuid = BuildConfig.APP_UUID
    @Synchronized
    private fun getClient(pcrCallback: PcrCallback? = null): OkHttpClient {
        return client ?: OkHttpClient.Builder()
            .enclavesTrustManager(
                if (pcrCallback != null) {
                    AttestationData(enclaveName, pcrCallback)
                } else {
                    AttestationData(
                        enclaveName,
                        PCRs(
                            pcr0 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                            pcr1 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                            pcr2 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                            pcr8 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        )
                    )
                },
                appUuid,
                enclaveURL
            )
            .build()
            .also { client = it }
    }

    fun staticPCRsEnclaveRequest(): String {
        val request = Request.Builder()
            .url("https://$enclaveURL/health")
            .get()
            .build()

        return try {
            getClient().newCall(request).execute().use { response ->
                response.body?.string() ?: throw IOException("Response body was null")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    fun cachedPCRsEnclaveRequest(): String {
        val pcrClient = OkHttpClient.Builder().build()
        val pcrRequest = Request.Builder()
            .url(BuildConfig.PCR_CALLBACK_URL)
            .build()

        val pcrCallback: PcrCallback = {
            val pcrResponse = pcrClient.newCall(pcrRequest).execute()
            val type = object : TypeToken<List<PCRs>>() {}.type
            val responseMap: List<PCRs> = Gson().fromJson(pcrResponse.body!!.string(), type)
            responseMap
        }

        val request = Request.Builder()
            .url("https://$enclaveURL/health")
            .get()
            .build()

        return try {
            getClient(pcrCallback).newCall(request).execute().use { response ->
                response.body?.string() ?: throw IOException("Response body was null")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }
}

