package com.evervault.sdk.core.utils

import crc32
import io.ktor.util.decodeBase64Bytes
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

class CRC32Test {

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun testCrc32() {
        val imageData = "ZW5jcnlwdGVk".decodeBase64Bytes()
        val crc32Hash = crc32(imageData)
        val first = imageData.get(0).toInt()
        print(first)
        assertEquals(1467498611, crc32Hash)
    }
}
