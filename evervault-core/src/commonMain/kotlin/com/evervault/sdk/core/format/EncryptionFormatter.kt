package com.evervault.sdk.core.format

import com.evervault.sdk.core.DataType

interface EncryptionFormatter {
    fun formatEncryptedData(dataType: DataType, keyIv: String, encryptedData: String): String
}
