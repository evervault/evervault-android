package com.evervault.sdk.common.keys

import com.evervault.sdk.common.DataCipher
import com.evervault.sdk.common.crypto.JvmDataCipher

internal object EvervaultFactory {
    fun createSharedSecretDeriver(): SharedSecretDeriver {
        return JvmSharedSecretDeriver()
    }

    fun createDataCipherFactory(): DataCipher.Factory {
        return JvmDataCipher.Factory
    }
}