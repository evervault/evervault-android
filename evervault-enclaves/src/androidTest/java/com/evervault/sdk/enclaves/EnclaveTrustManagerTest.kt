package com.evervault.sdk.enclaves

import AttestationDoc
import android.util.Base64
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import javax.net.ssl.SSLHandshakeException
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class OkHttpClientBuilderExtensionTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        // Generate a self-signed certificate for the mock server
        val rootCertificate = HeldCertificate.Builder()
            .certificateAuthority(1)
            .build()

        val serverCertificate = HeldCertificate.Builder()
            .addSubjectAlternativeName("localhost")
            .signedBy(rootCertificate)
            .build()

        val serverHandshakeCertificates = HandshakeCertificates.Builder()
            .heldCertificate(serverCertificate, rootCertificate.certificate)
            .build()

        mockWebServer = MockWebServer()
        mockWebServer.useHttps(serverHandshakeCertificates.sslSocketFactory(), false)
        mockWebServer.start()
    }

    @Test
    fun enclavesTrustManagerConfiguresClient() = runTest(testDispatcher, timeout = 30.seconds) {
        val builder = okhttp3.OkHttpClient.Builder()
        val attestationData = AttestationData(
            enclaveName = "test-enclave",
            pcrs = listOf(
                PCRs(
                    pcr0 = "pcr0",
                    pcr1 = "pcr1",
                    pcr2 = "pcr2",
                    pcr8 = "pcr8"
                )
            ),
            pcrCallback = null,
            callbackInterval = 0
        )

        val testDoc = "test-attestation-doc"
        val encodedDoc = Base64.encodeToString(
            testDoc.toByteArray(),
            Base64.DEFAULT
        )
        val attestationDoc = AttestationDoc(attestationDoc = encodedDoc)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(Json.encodeToString(attestationDoc))
        )

        val result = builder.enclavesTrustManager(
            enclaveAttestationData = attestationData,
            appUuid = "test-uuid",
            enclaveURL = mockWebServer.url("/.well-known/attestation").toString()
        )

        assertNotNull(result)
    }

    @Test(expected = SSLHandshakeException::class)
    fun enclavesTrustManagerThrowsOnAttestationRetrievalFailure() = runTest(testDispatcher, timeout = 30.seconds) {
        val builder = okhttp3.OkHttpClient.Builder()
        val attestationData = AttestationData(
            enclaveName = "test-enclave",
            pcrs = listOf(
                PCRs(
                    pcr0 = "pcr0",
                    pcr1 = "pcr1",
                    pcr2 = "pcr2",
                    pcr8 = "pcr8"
                )
            ),
            pcrCallback = null,
            callbackInterval = 0
        )

        // Mock a failed attestation response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Attestation retrieval failed")
                .addHeader("Content-Type", "text/plain")
        )

        val enclaveClient = builder.enclavesTrustManager(
            enclaveAttestationData = attestationData,
            appUuid = "test-uuid",
            enclaveURL = mockWebServer.url("/.well-known/attestation").toString()
        ).build()

        // Create a test request to trigger certificate validation
        val request = Request.Builder()
            .url(mockWebServer.url("/some-endpoint").toString())
            .get()
            .build()

        // This should throw CertificateException due to attestation failure
        enclaveClient.newCall(request).execute().use { response ->
            response.body?.string() ?: throw IOException("Response body was null")
        }

        // Verify that the attestation endpoint was called
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("/.well-known/attestation", recordedRequest.path)
        assertEquals("GET", recordedRequest.method)
    }
}