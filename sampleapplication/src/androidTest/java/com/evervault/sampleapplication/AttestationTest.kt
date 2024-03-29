package com.evervault.sampleapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.evervault.sdk.enclaves.AttestationData
import com.evervault.sdk.enclaves.PCRs
import com.evervault.sdk.enclaves.enclavesTrustManager
import okhttp3.OkHttpClient
import okhttp3.Request

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import javax.net.ssl.SSLHandshakeException

@RunWith(AndroidJUnit4::class)
class AttestationTest {

    private val enclaveName = "synthetic-cage"
    private val appUuid = "app-f5f084041a7e"
    private val url = "https://$enclaveName.$appUuid.cage.evervault.com/hello"
    private val betaUrl = "https://$enclaveName.$appUuid.cages.evervault.com/hello"
    @Test
    fun testSuccessfulAttestation() {
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
            .build()

        val response = client.newCall(request).execute()
        // 401 means TLS handshake was successful
        assertEquals(response.code, 401)
    }

    @Test
    fun testSuccessfulAttestationWithSinglePCR() {
        val client = OkHttpClient.Builder()
            .enclavesTrustManager(
                AttestationData(
                    enclaveName = enclaveName,
                    // Replace with legitimate PCR strings when not in debug mode
                    PCRs(
                        pcr8 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                    )
                ),
                appUuid
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        // 401 means TLS handshake was successful
        assertEquals(response.code, 401)
    }

    @Test
    fun testIncorrectPCRs() {
        val client = OkHttpClient.Builder()
            .enclavesTrustManager(
                AttestationData(
                    enclaveName = enclaveName,
                    // Replace with legitimate PCR strings when not in debug mode
                    PCRs(
                        pcr0 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                        pcr1 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                        pcr2 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                        pcr8 = "Incorrect"
                    )
                ),
                appUuid
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            client.newCall(request).execute()
            fail("Expected an exception to be thrown")
        } catch (e: SSLHandshakeException) {
            assertEquals(e.message, "Attestation failed")
        }
    }
}