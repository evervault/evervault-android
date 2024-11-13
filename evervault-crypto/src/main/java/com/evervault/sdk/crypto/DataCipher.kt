package com.evervault.sdk.crypto

import com.evervault.sdk.crypto.models.EncryptedData


internal interface DataCipher {
    fun encrypt(data: ByteArray, role: String?): EncryptedData

    interface Factory {
        fun createCipher(ecdhTeamKey: ByteArray, derivedSecret: ByteArray, config: com.evervault.sdk.crypto.EncryptionConfig): DataCipher
    }
}