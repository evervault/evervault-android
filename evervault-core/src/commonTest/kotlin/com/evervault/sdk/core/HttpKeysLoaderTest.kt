package com.evervault.sdk.core

import com.evervault.sdk.ConfigUrls
import com.evervault.sdk.HttpConfig
import com.evervault.sdk.core.keys.CageKey
import com.evervault.sdk.test.getenv
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class HttpKeysLoaderTest {
    @Test
    fun testLoadKeys() = runBlocking {
        val http = Http(
            config = HttpConfig(keysUrl = ConfigUrls().keysUrl),
            teamId = getenv("VITE_EV_TEAM_UUID"),
            appId = getenv("VITE_EV_APP_UUID"),
            context = "default"
        )
        val cageKey = http.loadKeys()

        assertEquals(
            cageKey,
            CageKey(publicKey = cageKey.ecdhP256KeyUncompressed, isDebugMode = cageKey.isDebugMode)
        )
    }
}
