package com.evervault.sdk.core.format

import com.evervault.sdk.core.DataType
import crc32
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class R1StdEncryptionFormatter(
    private val evVersion: String,
    private val publicKey: ByteArray,
    private val isDebug: Boolean
): EncryptionFormatter {

    @OptIn(ExperimentalEncodingApi::class)
    override fun formatEncryptedData(dataType: DataType, keyIv: ByteArray, encryptedData: String): String {
        // QkTC is a hardcoded version. Typically we base64 encode the version.
        return "ev:${if(isDebug) "debug:" else ""}${evVersion}${dataType.prefix}:${Base64.encode(keyIv).paddingRemoved}:${publicKey.encodeBase64().paddingRemoved}:${encryptedData.paddingRemoved}:$"
    }

    override fun formatFile(keyIv: ByteArray, encryptedData: ByteArray): ByteArray {
        val evEncryptedFileIdentifier: ByteArray = byteArrayOf(0x25, 0x45, 0x56, 0x45, 0x4e, 0x43)
        val versionNumber: ByteArray = byteArrayOf(0x03)
        val offsetToData: ByteArray = byteArrayOf(0x37, 0x00)
        val flags: ByteArray = byteArrayOf(0x00)

        val fileContents = evEncryptedFileIdentifier + versionNumber + offsetToData + publicKey + keyIv + flags + encryptedData

        val crc32Hash = crc32(fileContents)
        val crc32HashBytes = byteArrayOf(
            ((crc32Hash ushr 0) and 0xFF).toByte(),
            ((crc32Hash ushr 8) and 0xFF).toByte(),
            ((crc32Hash ushr 16) and 0xFF).toByte(),
            ((crc32Hash ushr 24) and 0xFF).toByte()
        )

        return fileContents + crc32HashBytes
    }
}

private val DataType.prefix: String
    get() = when(this) {
        DataType.STRING -> ""
        DataType.BOOLEAN, DataType.NUMBER -> ":$header"
    }

private val String.paddingRemoved: String get() {
    var i = length - 1
    while (i > 0) {
        if (this[i] != '=') {
            break
        }
        i--
    }
    return substring(0, ++i)
}
