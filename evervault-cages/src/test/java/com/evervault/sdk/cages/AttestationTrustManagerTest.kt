package com.evervault.sdk.cages

import AttestationDocCache
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

class AttestationTrustManagerTest {

    @Mock
    private lateinit var attestationDocCache: AttestationDocCache
    private lateinit var attestationTrustManager: AttestationTrustManagerGA
    private lateinit var attestationData: AttestationData

    val cageName = "test-cage"
    val appUuid ="app-uuid"
    val PCRs = PCRs("0000", "1111", "2222", "3333")
    val attestationDoc = ByteArray(0)
    val attestCageCallback: AttestCageCallback = { _, _, _ ->
        true
    }
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        attestationData = AttestationData(cageName, PCRs)
        attestationTrustManager = AttestationTrustManagerGA(attestationData, attestationDocCache, attestCageCallback)
    }

    @Test(expected = CertificateException::class)
    fun `throws expection when certificate chain isn't present`() {
        attestationTrustManager.checkServerTrusted(emptyArray(), null)
    }

    @Test
    fun `no excpetion thrown when setting implicit PCRs`() {
        val mockCert = Mockito.mock(X509Certificate::class.java)
        Mockito.`when`(mockCert.encoded).thenReturn(ByteArray(1))
        val mockCertArray: Array<X509Certificate> = arrayOf(mockCert)
        Mockito.`when`(attestationDocCache.get()).thenReturn(attestationDoc)

        attestationTrustManager.checkServerTrusted(mockCertArray, null)
    }

    @Test
    fun `passing pcr callback doesn't use implicit PCRs`() {
        val PCRs = listOf(PCRs("0000", "1111", "2222", "3333"))
        val spyPCRs = spy<PcrCallback>()
        Mockito.`when`(spyPCRs.invoke()).thenReturn(PCRs)
        attestationData = AttestationData(cageName, spyPCRs)
        attestationTrustManager = AttestationTrustManagerGA(attestationData, attestationDocCache, attestCageCallback)
        val mockCert = Mockito.mock(X509Certificate::class.java)
        Mockito.`when`(mockCert.encoded).thenReturn(ByteArray(1))
        val mockCertArray: Array<X509Certificate> = arrayOf(mockCert)
        Mockito.`when`(attestationDocCache.get()).thenReturn(attestationDoc)

        attestationTrustManager.checkServerTrusted(mockCertArray, null)

        Mockito.verify(spyPCRs, times(1)).invoke()
    }
}