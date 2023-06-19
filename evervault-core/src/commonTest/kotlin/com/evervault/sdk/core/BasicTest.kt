package com.evervault.sdk.core

import com.evervault.sdk.CustomConfig
import com.evervault.sdk.Evervault
import com.evervault.sdk.test.getenv
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test

class BasicTest {

    @BeforeTest
    fun setup() {
        Evervault.shared.configure(
            teamId = "",
            appId = "",
            customConfig = CustomConfig(isDebugMode = true)
        )
    }

    @Test
    fun testEncryptString() = runBlocking {
        val encrypted = Evervault.shared.encrypt("Foo")
        println(encrypted)
    }
}
