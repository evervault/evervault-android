package com.evervault.sdk.crypto.format

import com.evervault.sdk.crypto.DataType

internal interface EncryptionFormatter {
    fun formatEncryptedData(dataType: DataType, keyIv: ByteArray, encryptedData: String): String
    fun formatFile(keyIv: ByteArray, encryptedData: ByteArray): ByteArray
}