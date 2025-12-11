package com.evervault.sdk.core

import com.evervault.sdk.ConfigUrls
import com.evervault.sdk.HttpConfig
import com.evervault.sdk.core.keys.CageKey
import com.evervault.sdk.test.getAppUUID
import com.evervault.sdk.test.getTeamUUID
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class HttpKeysLoaderTest {
    @Test
    fun testLoadKeys() = runTest {
        val http = Http(
            config = HttpConfig(
                keysUrl = ConfigUrls().keysUrl,
                apiUrl = ConfigUrls().apiUrl
            ),
            teamId = getTeamUUID(),
            appId = getAppUUID(),
            context = "default"
        )
        val cageKey = http.loadKeys()
        assertEquals(
            cageKey,
            CageKey(publicKey = cageKey.ecdhP256KeyUncompressed, isDebugMode = cageKey.isDebugMode)
        )
    }
}
