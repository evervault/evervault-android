package com.evervault.sdk.common.keys

import org.junit.Assert.assertEquals
import org.junit.Test

class KeyTest {
    private val publicKey = "BF1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ2xdoyGpXYrplO/f8AZ+7cGkUnMF3tzSfLC5Io8BuNyw="

    @Test
    fun testPublicKey() {
        val key = Key(publicKey = publicKey)
        assertEquals(key.ecdhP256KeyUncompressed, publicKey)
        assertEquals(key.ecdhP256Key, "Al1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ")
    }
}