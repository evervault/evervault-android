package com.evervault.sdk.core.utils

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

class EcPointCompressTest {

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun testCompress() {
        val uncompressed = "BF1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ2xdoyGpXYrplO/f8AZ+7cGkUnMF3tzSfLC5Io8BuNyw="
        val keyData = Base64.decode(uncompressed)
        val compressed = ecPointCompress(keyData)
        val base64Compressed = Base64.encode(compressed)
        assertEquals("Al1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ", base64Compressed)
    }
}