package com.evervault.sdk.common.crypto

import com.evervault.sdk.common.DataCipher
import com.evervault.sdk.common.EncryptionConfig
import com.evervault.sdk.common.keys.EvervaultFactory
import com.evervault.sdk.common.keys.GeneratedSharedKey
import org.junit.Before
import com.evervault.sdk.common.keys.Key
import org.bouncycastle.util.Arrays
import org.junit.Test
import org.junit.Assert.*
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class JVMDataCipherTest {
    private val key = Key("BF1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ2xdoyGpXYrplO/f8AZ+7cGkUnMF3tzSfLC5Io8BuNyw=")
    private val sharedSecretDeriver = EvervaultFactory.createSharedSecretDeriver()
    private val dataCipherFactory = EvervaultFactory.createDataCipherFactory()
    private val config =  EncryptionConfig()

    lateinit var sharedSecret: GeneratedSharedKey
    lateinit var teamKeyPublic: ByteArray
    private lateinit var dataCipher: DataCipher

    @OptIn(ExperimentalEncodingApi::class)
    @Before
    fun setup() {
        sharedSecret = sharedSecretDeriver.deriveSharedSecret(key)
        teamKeyPublic = Base64.decode(key.ecdhP256Key)
        dataCipher = dataCipherFactory.createCipher(teamKeyPublic, sharedSecret.sharedKey, config)
    }

    @Test
    fun testEncrypt() {
        val testPayload = "Encrypt this String".toByteArray()
        val result = dataCipher.encrypt(testPayload, "test-role")

        assertNotNull(result)
    }

    @Test
    fun testRoleMetadataIncreasesCipherTextSize() {
        val testPayload = "Encrypt this String".toByteArray()
        val roleEncryptionResult = dataCipher.encrypt(testPayload, "test-role")
        val nonRoleEncryptionResult = dataCipher.encrypt(testPayload, null)

        assertTrue(roleEncryptionResult.data.size > nonRoleEncryptionResult.data.size)
    }

    @Test
    fun testEncryptionUniqueness() {
        val testPayload = "Encrypt this String".toByteArray()
        val resultOne = dataCipher.encrypt(testPayload, "test-role")
        val resultTwo = dataCipher.encrypt(testPayload, "test-role")

        val isEqual = Arrays.areEqual(resultOne.data, resultTwo.data)

        assertFalse(isEqual)
    }
}