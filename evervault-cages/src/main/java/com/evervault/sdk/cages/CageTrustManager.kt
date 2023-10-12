package com.evervault.sdk.cages

import AttestationDocCache
import okhttp3.OkHttpClient
import uniffi.bindings.PcRs
import uniffi.bindings.attestCage
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class AttestationTrustManagerGA(private val cageAttestationData: AttestationData, private val cache: AttestationDocCache) : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw UnsupportedOperationException("Client certificates not supported!")
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        if (chain == null || chain.isEmpty()) throw CertificateException("Empty or null certificate chain")
        val serverCertificate = chain[0]

        val remoteCertificateData = serverCertificate.encoded

        val attestationDoc : ByteArray = cache.get()

        val result = attestCage(remoteCertificateData, cageAttestationData.pcrs.map {
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

fun OkHttpClient.Builder.cagesTrustManager(cageAttestationData: AttestationData, appUuid: String): OkHttpClient.Builder {
    val cache = AttestationDocCache(cageAttestationData.cageName, appUuid)
    val trustManager = AttestationTrustManagerGA(cageAttestationData, cache)
    val sslContext = SSLContext.getInstance("TLSv1.2")
    sslContext.init(null, arrayOf(trustManager), SecureRandom())

    return this
        .sslSocketFactory(sslContext.socketFactory, trustManager)
}