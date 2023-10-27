package com.evervault.sdk.common

import com.evervault.sdk.common.models.EncryptedData


internal interface DataCipher {
    fun encrypt(data: ByteArray, role: String?): EncryptedData

    interface Factory {
        fun createCipher(ecdhTeamKey: ByteArray, derivedSecret: ByteArray, config: EncryptionConfig): DataCipher
    }
}