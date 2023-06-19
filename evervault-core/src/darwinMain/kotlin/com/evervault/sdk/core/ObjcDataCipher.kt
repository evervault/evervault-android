package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig
import platform.Security.*
import platform.Foundation.*
import kotlinx.cinterop.*
import com.evervault.sdk.objclibs.kcrypto.*
import com.evervault.sdk.toNSData
import com.evervault.sdk.core.exceptions.EncryptionException

internal class ObjcDataCipher(
    private val ecdhTeamKey: ByteArray,
    private val derivedSecret: ByteArray,
    private val config: EncryptionConfig
): DataCipher {

    companion object Factory: DataCipher.Factory {
        override fun createCipher(ecdhTeamKey: ByteArray, derivedSecret: ByteArray, config: EncryptionConfig): DataCipher {
            return ObjcDataCipher(
                ecdhTeamKey = ecdhTeamKey,
                derivedSecret = derivedSecret,
                config = config,
            )
        }

    }

    override fun encrypt(data: ByteArray): EncryptedData {
        val keyIv = generateBytes(config.ivLength)

        return memScoped {
            val error = alloc<ObjCObjectVar<NSError?>>()
            val sealedBox = SealedBoxWrapper(data.toNSData(), ecdhTeamKey.toNSData(), derivedSecret.toNSData(), keyIv, error.ptr)

            val ciphertext: NSData = sealedBox.ciphertext()
            val tag: NSData = sealedBox.tag()
            val combined = NSMutableData().apply {
                appendBytes(ciphertext.bytes, ciphertext.length)
                appendBytes(tag.bytes, tag.length)
            }

            val nsError: NSError? = error.value
            if (nsError != null) {
                throw EncryptionException(nsError.localizedDescription)
            }

             EncryptedData(
                combined.bytes!!.readBytes(combined.length.toInt()),
                keyIv.bytes!!.readBytes(keyIv.length.toInt())
            )
        }
    }

}

@Throws(CryptoException::class)
private fun generateBytes(byteLength: Int): NSData {
    val randomBytes = NSMutableData.dataWithLength(byteLength.toULong()) as NSMutableData

    val result = SecRandomCopyBytes(kSecRandomDefault, byteLength.toULong(), randomBytes.mutableBytes)
    if (result != errSecSuccess) {
        throw CryptoException("Random generation failed")
    }

    return randomBytes
}

class CryptoException(message: String): Exception(message)
