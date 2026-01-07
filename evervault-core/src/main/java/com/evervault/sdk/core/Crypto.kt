package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig
import com.evervault.sdk.core.exceptions.ExceededMaxFileSizeException
import com.evervault.sdk.core.format.EncryptionFormatter
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal class Crypto(
    private val encryptionFormatter: EncryptionFormatter,
    private val dataCipher: DataCipher,
    private val config: EncryptionConfig
) : EncryptionService {

    override fun encryptString(string: String, dataType: DataType, role: String?, type: String?): String {
        return encrypt(string.toByteArray(), role, type) { encryptedData, keyIv ->
            encryptionFormatter.formatEncryptedData(
                dataType,
                keyIv,
                Base64.encode(encryptedData)
            )
        }
    }

    override fun encryptData(data: ByteArray, role: String?, dataType: String?): ByteArray {
        require(data.size <= config.maxFileSizeInBytes) {
            throw ExceededMaxFileSizeException(config.maxFileSizeInMB)
        }
        return encrypt(data, null, null) { encryptedData, keyIv ->
            encryptionFormatter.formatFile(keyIv, encryptedData)
        }
    }

    private fun <T> encrypt(data: ByteArray, role: String?, dataType: String? = null, format: (ByteArray, ByteArray) -> T): T {
        val encryptedData = dataCipher.encrypt(data, role, dataType)
        return format(encryptedData.data, encryptedData.keyIv)
    }
}
