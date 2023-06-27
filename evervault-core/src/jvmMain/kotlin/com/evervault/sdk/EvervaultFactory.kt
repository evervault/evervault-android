package com.evervault.sdk

import com.evervault.sdk.core.DataCipher
import com.evervault.sdk.core.JvmDataCipher
import com.evervault.sdk.core.keys.JvmSharedSecretDeriver
import com.evervault.sdk.core.keys.SharedSecretDeriver

internal actual object EvervaultFactory {
    actual fun createSharedSecretDeriver(): SharedSecretDeriver {
        return JvmSharedSecretDeriver()
    }

    actual fun createDataCipherFactory(): DataCipher.Factory {
        return JvmDataCipher.Factory
    }
}