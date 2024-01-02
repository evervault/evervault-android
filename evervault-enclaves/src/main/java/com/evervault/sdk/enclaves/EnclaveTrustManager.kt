package com.evervault.sdk.enclaves

import AttestationDocCache
import okhttp3.OkHttpClient
import uniffi.bindings.PcRs
import uniffi.bindings.attestEnclave
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

typealias AttestEnclaveCallback = (remoteCertificateData: ByteArray, expectedPCRs: List<PcRs>, attestationDoc: ByteArray) -> Boolean

class AttestationTrustManagerGA(private val enclaveAttestationData: AttestationData, private val cache: AttestationDocCache, private val attestEnclaveCallback: AttestEnclaveCallback) : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw UnsupportedOperationException("Client certificates not supported!")
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        if (chain == null || chain.isEmpty()) throw CertificateException("Empty or null certificate chain")
        val serverCertificate = chain[0]

        val remoteCertificateData = serverCertificate.encoded

        val attestationDoc : ByteArray = cache.get()

        if (enclaveAttestationData.pcrCallback !== null) {
            val enclavePcrManager = EnclavePCRManager.getInstance(enclaveAttestationData.callbackInterval)
            enclavePcrManager.invoke(enclaveAttestationData.enclaveName, enclaveAttestationData.pcrCallback)
            attestPCRs(remoteCertificateData, enclavePcrManager.getPCRs(enclaveAttestationData.enclaveName), attestationDoc)
        } else {
            attestPCRs(remoteCertificateData, enclaveAttestationData.pcrs, attestationDoc)
        }
    }

    private fun attestPCRs(remoteCertificateData: ByteArray, expectedPCRs: List<PCRs>, attestationDoc: ByteArray) {
        val result = attestEnclaveCallback(remoteCertificateData, expectedPCRs.map {
            PcRs(
                it.pcr0,
                it.pcr1,
                it.pcr2,
                it.pcr8,
            )
        }, attestationDoc)
        if (!result) {
            throw CertificateException("Attestation failed")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}

fun OkHttpClient.Builder.enclavesTrustManager(enclaveAttestationData: AttestationData, appUuid: String): OkHttpClient.Builder {
    val cache = AttestationDocCache(enclaveAttestationData.enclaveName, appUuid)
    val attestEnclaveCallback: AttestEnclaveCallback = { remoteCertificateData, expectedPCRs, attestationDoc ->
        attestEnclave(remoteCertificateData, expectedPCRs, attestationDoc)
    }
    val trustManager = AttestationTrustManagerGA(enclaveAttestationData, cache, attestEnclaveCallback)
    val sslContext = SSLContext.getInstance("TLSv1.2")
    sslContext.init(null, arrayOf(trustManager), SecureRandom())

    return this
        .sslSocketFactory(sslContext.socketFactory, trustManager)
}