package com.evervault.sdk.crypto.keys

import com.evervault.sdk.crypto.crypto.DataCipher

internal object EvervaultFactory {
    fun createSharedSecretDeriver(): SharedSecretGenerator {
        return SharedSecretGenerator()
    }

    fun createDataCipherFactory(): DataCipher.Factory {
        return DataCipher.Factory
    }
}