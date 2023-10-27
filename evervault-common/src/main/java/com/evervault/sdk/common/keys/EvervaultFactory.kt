package com.evervault.sdk.common.keys

import com.evervault.sdk.common.crypto.DataCipher

internal object EvervaultFactory {
    fun createSharedSecretDeriver(): SharedSecretGenerator {
        return SharedSecretGenerator()
    }

    fun createDataCipherFactory(): com.evervault.sdk.common.DataCipher.Factory {
        return DataCipher.Factory
    }
}