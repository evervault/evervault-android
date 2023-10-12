package com.evervault.sdk.cages

import okhttp3.OkHttpClient
import uniffi.bindings.PcRs
import uniffi.bindings.attestConnection
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class AttestationTrustManager(private val cageAttestationData: AttestationData) : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        throw UnsupportedOperationException("Client certificates not supported!")
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        if (chain == null || chain.isEmpty()) throw CertificateException("Empty or null certificate chain")
        val serverCertificate = chain[0]

        val remoteCertificateData = serverCertificate.encoded

        val result = attestConnection(remoteCertificateData, cageAttestationData.pcrs.map {
            PcRs(
                it.pcr0,
                it.pcr1,
                it.pcr2,
                it.pcr8,
            )
        })
        if (!result) {
            throw CertificateException("Attestation failed")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }
}
@Deprecated("This function is deprecated, use cageTrustManager instead")

fun OkHttpClient.Builder.trustManager(cageAttestationData: AttestationData): OkHttpClient.Builder {
    val trustManager = AttestationTrustManager(cageAttestationData)
    val sslContext = SSLContext.getInstance("TLSv1.2")
    sslContext.init(null, arrayOf(trustManager), SecureRandom())

    return this
        .sslSocketFactory(sslContext.socketFactory, trustManager)
}
