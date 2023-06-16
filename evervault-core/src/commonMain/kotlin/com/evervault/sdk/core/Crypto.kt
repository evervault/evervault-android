package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig
import com.evervault.sdk.core.format.EncryptionFormatter
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal class Crypto(
    private val encryptionFormatter: EncryptionFormatter,
    private val dataCipher: DataCipher,
    private val config: EncryptionConfig,
) : EncryptionService {

    override fun encryptString(string: String, dataType: DataType): String {
        return encrypt(string.toByteArray()) { encryptedData, keyIv ->
            encryptionFormatter.formatEncryptedData(
                dataType,
                Base64.encode(keyIv),
                Base64.encode(encryptedData)
            )
        }
    }

//    override fun encryptData(data: ByteArray): ByteArray {
//        require(data.size <= config.maxFileSizeInBytes) {
//            throw CryptoError.ExceededMaxFileSizeError(config.maxFileSizeInMB)
//        }
//
//        return encrypt(data) { encryptedData, keyIv ->
//            formatFile(
//                keyIv = keyIv,
//                ecdhPublicKey = ecdhPublicKey,
//                encryptedData = encryptedData
//            )
//        }
//    }

    private fun encrypt(data: ByteArray, format: (ByteArray, ByteArray) -> String): String {
        val encryptedData = dataCipher.encrypt(data)
        return format(encryptedData.data, encryptedData.keyIv)
    }
}

//private fun formatFile(
//    keyIv: ByteArray,
//    ecdhPublicKey: KeyAgreement.PublicKey,
//    encryptedData: ByteArray
//): ByteArray {
//    val evEncryptedFileIdentifier: ByteArray = byteArrayOf(0x25, 0x45, 0x56, 0x45, 0x4e, 0x43)
//    val versionNumber: ByteArray = byteArrayOf(0x03)
//    val offsetToData: ByteArray = byteArrayOf(0x37, 0x00)
//    val flags: ByteArray = byteArrayOf(0x00)
//
//    val exportableEcdhPublicKey = ecdhPublicKey.encoded
//    val compressedKey = ecPointCompress(exportableEcdhPublicKey)
//
//    val fileContents = ByteBuffer.allocate(
//        evEncryptedFileIdentifier.size +
//                versionNumber.size +
//                offsetToData.size +
//                compressedKey.size +
//                keyIv.size +
//                flags.size +
//                encryptedData.size
//    )
//        .put(evEncryptedFileIdentifier)
//        .put(versionNumber)
//        .put(offsetToData)
//        .put(compressedKey)
//        .put(keyIv)
//        .put(flags)
//        .put(encryptedData)
//        .array()
//
//    val crc32Hash = crc32(fileContents)
//    val crc32HashBytes = ByteBuffer.allocate(4)
//        .putInt(crc32Hash)
//        .array()
//
//    return fileContents + crc32HashBytes
//}
