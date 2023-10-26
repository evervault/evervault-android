package com.evervault.sdk.common.format

import com.evervault.sdk.common.DataType

internal interface EncryptionFormatter {
    fun formatEncryptedData(dataType: DataType, keyIv: ByteArray, encryptedData: String): String
    fun formatFile(keyIv: ByteArray, encryptedData: ByteArray): ByteArray
}