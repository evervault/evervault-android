package com.evervault.sdk.crypto

import com.evervault.sdk.crypto.exceptions.ExceededMaxFileSizeException
import com.evervault.sdk.crypto.format.EncryptionFormatter
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal class Crypto(
    private val encryptionFormatter: EncryptionFormatter,
    private val dataCipher: com.evervault.sdk.crypto.DataCipher,
    private val config: com.evervault.sdk.crypto.EncryptionConfig
) : com.evervault.sdk.crypto.EncryptionService {

    override fun encryptString(string: String, dataType: com.evervault.sdk.crypto.DataType, role: String?): String {
        return encrypt(string.toByteArray(), role) { encryptedData, keyIv ->
            encryptionFormatter.formatEncryptedData(
                dataType,
                keyIv,
                Base64.encode(encryptedData)
            )
        }
    }

    override fun encryptData(data: ByteArray, role: String?): ByteArray {
        require(data.size <= config.maxFileSizeInBytes) {
            throw ExceededMaxFileSizeException(config.maxFileSizeInMB)
        }

        return encrypt(data, null) { encryptedData, keyIv ->
            encryptionFormatter.formatFile(keyIv, encryptedData)
        }
    }

    private fun <T> encrypt(data: ByteArray, role: String?, format: (ByteArray, ByteArray) -> T): T {
        val encryptedData = dataCipher.encrypt(data, role)
        return format(encryptedData.data, encryptedData.keyIv)
    }
}