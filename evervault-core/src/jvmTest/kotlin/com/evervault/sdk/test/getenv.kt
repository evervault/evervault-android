package com.evervault.sdk.test

actual fun getenv(key: String): String = System.getenv(key)
