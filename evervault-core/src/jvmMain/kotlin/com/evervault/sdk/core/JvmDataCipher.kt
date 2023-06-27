package com.evervault.sdk.core

import com.evervault.sdk.EncryptionConfig
import com.evervault.sdk.core.exceptions.InvalidCipherException
import org.bouncycastle.crypto.InvalidCipherTextException
import org.bouncycastle.crypto.engines.AESEngine
import org.bouncycastle.crypto.modes.GCMBlockCipher
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.KeyParameter
import java.security.SecureRandom

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

    override fun encrypt(data: ByteArray): EncryptedData {
        val keyIv = generateBytes(config.ivLength)

        val cipher = GCMBlockCipher(AESEngine())
        val compressedTeamPublicKey = ecdhTeamKey

        val parameters = AEADParameters(KeyParameter(derivedSecret), config.authTagLength, keyIv, compressedTeamPublicKey)
        cipher.init(true, parameters)

        val cipherText = ByteArray(cipher.getOutputSize(data.size))
        val len = cipher.processBytes(data, 0, data.size, cipherText, 0)

        try {
            cipher.doFinal(cipherText, len)
        } catch (e: InvalidCipherTextException) {
            // We don't want to expose Bouncy Castle to the user.
            throw InvalidCipherException(e)
        }

        return EncryptedData(cipherText, keyIv)
    }

}

private fun generateBytes(byteLength: Int): ByteArray {
    val randomBytes = ByteArray(byteLength)
    SecureRandom().nextBytes(randomBytes)
    return randomBytes
}
