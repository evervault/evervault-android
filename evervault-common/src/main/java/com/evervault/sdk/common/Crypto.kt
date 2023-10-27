package com.evervault.sdk.common

import com.evervault.sdk.common.exceptions.ExceededMaxFileSizeException
import com.evervault.sdk.common.format.EncryptionFormatter
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal class Crypto(
    private val encryptionFormatter: EncryptionFormatter,
    private val dataCipher: DataCipher,
    private val config: EncryptionConfig
) : EncryptionService {

    override fun encryptString(string: String, dataType: DataType, role: String?): String {
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