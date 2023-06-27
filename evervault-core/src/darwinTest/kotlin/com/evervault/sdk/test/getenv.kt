package com.evervault.sdk.test

import platform.Foundation.NSProcessInfo

actual fun getenv(key: String): String {
    return NSProcessInfo.processInfo.environment[key] as String
}
