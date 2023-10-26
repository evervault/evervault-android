package com.evervault.sdk.common


internal interface DataCipher {
    fun encrypt(data: ByteArray, role: String?): EncryptedData

    interface Factory {
        fun createCipher(ecdhTeamKey: ByteArray, derivedSecret: ByteArray, config: EncryptionConfig): DataCipher
    }
}

// TODO: move to data class
data class EncryptedData(
    val data: ByteArray,
    val keyIv: ByteArray
)