package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig

internal interface DataCipher {
    fun encrypt(data: ByteArray, role: String?, dataType: String?): EncryptedData

    interface Factory {
        fun createCipher(ecdhTeamKey: ByteArray, ephemeralPublicKey: ByteArray, derivedSecret: ByteArray, isDebugMode: Boolean, config: EncryptionConfig): DataCipher
    }
}
