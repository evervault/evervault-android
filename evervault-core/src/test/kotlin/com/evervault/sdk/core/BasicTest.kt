package com.evervault.sdk.core

import com.evervault.sdk.CustomConfig
import com.evervault.sdk.Evervault
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicTest {

    @BeforeTest
    fun setup() {
        Evervault.shared.configure(
            teamId = System.getenv("VITE_EV_TEAM_UUID"),
            appId = System.getenv("VITE_EV_APP_UUID"),
            customConfig = CustomConfig(isDebugMode = true)
        )
    }

    @Test
    fun testEncryptString() = runBlocking {
        val encrypted = Evervault.shared.encrypt("Foo")
        println(encrypted)
    }
}
