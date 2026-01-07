package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig
import com.evervault.sdk.core.exceptions.InvalidCipherException
import org.bouncycastle.crypto.InvalidCipherTextException
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.modes.GCMBlockCipher
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.KeyParameter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom

internal class JvmDataCipher(
    private val ecdhTeamKey: ByteArray,
    private val ephemeralPublicKey: ByteArray,
    private val derivedSecret: ByteArray,
    private val isDebugMode: Boolean,
    private val config: EncryptionConfig
): DataCipher {

    companion object Factory: DataCipher.Factory {
        override fun createCipher(ecdhTeamKey: ByteArray, ephemeralPublicKey: ByteArray, derivedSecret: ByteArray, isDebugMode: Boolean, config: EncryptionConfig): DataCipher {
            return JvmDataCipher(
                ecdhTeamKey = ecdhTeamKey,
                ephemeralPublicKey = ephemeralPublicKey,
                derivedSecret = derivedSecret,
                isDebugMode = isDebugMode,
                config = config,
            )
        }

    }

    override fun encrypt(data: ByteArray, role: String?, dataType: String?): EncryptedData {
        val keyIv = generateBytes(config.ivLength)

        val cipher = GCMBlockCipher(AESEngine())
        val compressedTeamPublicKey = ecdhTeamKey
        var aad: ByteArray
        if (dataType == "ByteArray" || dataType == null) {
            aad = compressedTeamPublicKey
        } else {
            aad = createV2Aad(dataType)
        }
        val parameters = AEADParameters(KeyParameter(derivedSecret), config.authTagLength, keyIv, aad)
        cipher.init(true, parameters)

        var cipherData = data
        if (!(dataType == "ByteArray" && dataType == null)) {
            var metadata = generateMetadata(role)
            val metaDataOffset = generateMetadataOffset(metadata)
            cipherData = concatenateNByteArrays(listOf(metaDataOffset, metadata, data))
        }

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

    private fun createV2Aad(dataType: String): ByteArray {
        var dataTypeNumber = 0; // Default to String

        if (isNumberType(dataType)) {
            dataTypeNumber = 1;
        } else if (dataType == "Boolean") {
            dataTypeNumber = 2;
        }

        val versionNumber = 1; // Android SDK only has QkTC

        var configurationByte = if (isDebugMode) byteArrayOf(0x00) else byteArrayOf((0x00 or (dataTypeNumber shl 4) or versionNumber).toByte());
        val aad: ByteArray = configurationByte + ephemeralPublicKey + ecdhTeamKey;
        return aad;
    }

    private fun concatenateNByteArrays(nBytesArrays: List<ByteArray>): ByteArray {
        var concatenatedByteArray = byteArrayOf()
        for(byteArray in nBytesArrays) {
            concatenatedByteArray += byteArray
        }
        return concatenatedByteArray
    }
}

private fun isNumberType(type: String): Boolean {
    return type == "Number" || type == "UInt" || type == "UByte" || type == "UShort" || type == "ULong"
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
