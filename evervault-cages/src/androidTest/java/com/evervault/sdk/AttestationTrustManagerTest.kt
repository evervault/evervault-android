package com.evervault.sdk

import com.evervault.sdk.cages.AttestationData
import com.evervault.sdk.cages.PCRs
import com.evervault.sdk.cages.trustManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException

class ExampleUnitTDest {

    @Test
    fun test_test() {
        val cageName = "synthetic-cage"
        val appUuid = "app-f5f084041a7e"
        val url = "https://$cageName.$appUuid.cages.evervault.com/hello"
        println(url)
        var responseText: String? = "";
        val client = OkHttpClient.Builder()
            .trustManager(
                AttestationData(
                cageName = cageName,
                // Replace with legitimate PCR strings when not in debug mode
                PCRs(
                    pcr0 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    pcr1 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    pcr2 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                    pcr8 = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                )
            )
            )
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Request failed with status code: ${response.code}")
            } else {
                println("Response status code: ${response.code}")
            }
        }
    }
}
