package com.evervault.sdk.common.crypto

import com.evervault.sdk.common.DataCipher
import com.evervault.sdk.common.EncryptedData
import com.evervault.sdk.common.EncryptionConfig
import com.evervault.sdk.common.exceptions.InvalidCipherException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import org.bouncycastle.crypto.InvalidCipherTextException
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.modes.GCMBlockCipher
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.KeyParameter

internal class JvmDataCipher(
    private val ecdhTeamKey: ByteArray,
    private val derivedSecret: ByteArray,
    private val config: EncryptionConfig
): DataCipher {
    companion object Factory: DataCipher.Factory {
        override fun createCipher(ecdhTeamKey: ByteArray, derivedSecret: ByteArray, config: EncryptionConfig): DataCipher {
            return JvmDataCipher(
                ecdhTeamKey = ecdhTeamKey,
                derivedSecret = derivedSecret,
                config = config,
            )
        }

    }

    override fun encrypt(data: ByteArray, role: String?): EncryptedData {
        val keyIv = generateBytes(config.ivLength)

        val cipher = GCMBlockCipher(AESEngine())
        val compressedTeamPublicKey = ecdhTeamKey

        val parameters = AEADParameters(KeyParameter(derivedSecret), config.authTagLength, keyIv, compressedTeamPublicKey)
        cipher.init(true, parameters)

        val metadata = generateMetadata(role)
        val metaDataOffset = generateMetadataOffset(metadata)
        val cipherData = concatenateNByteArrays(listOf(metaDataOffset, metadata, data))

        val cipherText = ByteArray(cipher.getOutputSize(cipherData.size))
        val len = cipher.processBytes(cipherData, 0, cipherData.size, cipherText, 0)
        try {
            cipher.doFinal(cipherText, len)
        } catch (e: InvalidCipherTextException) {
            // We don't want to expose Bouncy Castle to the user.
            throw InvalidCipherException(e)
        }

        return EncryptedData(cipherText, keyIv)
    }

    private fun concatenateNByteArrays(nBytesArrays: List<ByteArray>): ByteArray {
        var concatenatedByteArray = byteArrayOf()
        for(byteArray in nBytesArrays) {
            concatenatedByteArray += byteArray
        }
        return concatenatedByteArray
    }
}

private fun generateBytes(byteLength: Int): ByteArray {
    val randomBytes = ByteArray(byteLength)
    SecureRandom().nextBytes(randomBytes)
    return randomBytes
}

private fun generateMetadata(role: String?): ByteArray {
    val buffer = mutableListOf<Byte>()

    buffer.add((0x80 or if (role.isNullOrEmpty()) 2 else 3).toByte())

    if (!role.isNullOrEmpty()) {
        // `dr` (data role) => role_name
        buffer.add(0xA2.toByte())
        buffer.addAll("dr".toByteArray().toList())
        buffer.add((0xA0 or role.length).toByte())
        buffer.addAll(role.toByteArray().toList())
    }

    // "eo" (encryption origin) => 12 (Android SDK)
    buffer.add(0xA2.toByte())
    buffer.addAll("eo".toByteArray().toList())
    buffer.add(10.toByte())

    // "et" (encryption timestamp) => current time
    buffer.add(0xA2.toByte())
    buffer.addAll("et".toByteArray().toList())
    buffer.add(0xCE.toByte())
    buffer.addAll(getCurrentEpochTimestampByteArray().toList())

    return buffer.toByteArray()
}

private fun generateMetadataOffset(metadata: ByteArray): ByteArray {
    val metadataLength = metadata.size
    val buffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
    buffer.putShort(metadataLength.toShort())
    return buffer.array()
}

private fun getCurrentEpochTimestampByteArray(): ByteArray {
    val timestamp = System.currentTimeMillis() / 1000
    val buffer = ByteBuffer.allocate(4)

    buffer.order(ByteOrder.LITTLE_ENDIAN)

    buffer.putInt(timestamp.toInt())

    return buffer.array()
}