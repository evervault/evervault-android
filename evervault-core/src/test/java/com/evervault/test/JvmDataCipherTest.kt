package com.evervault.sdk.test

import com.evervault.sdk.EncryptionConfig
import com.evervault.sdk.EvervaultFactory
import com.evervault.sdk.core.DataCipher
import com.evervault.sdk.core.keys.CageKey
import com.evervault.sdk.core.keys.GeneratedSharedKey
import org.bouncycastle.util.Arrays
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class JVMDataCipherTest {
    private val key = CageKey("BF1/Mo85D7t/XvC3I+YYpJvP+OsSyxIbSrhtDhg1SClQ2xdoyGpXYrplO/f8AZ+7cGkUnMF3tzSfLC5Io8BuNyw=")
    private val sharedSecretDeriver = EvervaultFactory.createSharedSecretDeriver()
    private val dataCipherFactory = EvervaultFactory.createDataCipherFactory()
    private val config =  EncryptionConfig()

    lateinit var sharedSecret: GeneratedSharedKey
    lateinit var teamKeyPublic: ByteArray
    private lateinit var dataCipher: DataCipher

    @OptIn(ExperimentalEncodingApi::class)
    @BeforeTest
    fun setup() {
        sharedSecret = sharedSecretDeriver.deriveSharedSecret(key)
        teamKeyPublic = Base64.decode(key.ecdhP256Key)
        dataCipher = dataCipherFactory.createCipher(teamKeyPublic, sharedSecret.generatedEcdhKey, sharedSecret.sharedKey, true, config)
    }

    @Test
    fun testEncrypt() {
        val testPayload = "Encrypt this String".toByteArray()
        val result = dataCipher.encrypt(testPayload, "test-role", "String")
        assertNotNull(result)
    }

    @Test
    fun testEncryptDataRole() {
        val testPayload = "Encrypt this String".toByteArray()
        val result = dataCipher.encrypt(testPayload, null, "String")
        assertNotNull(result)
    }

    @Test
    fun testRoleMetadataIncreasesCipherTextSize() {
        val testPayload = "Encrypt this String".toByteArray()
        val roleEncryptionResult = dataCipher.encrypt(testPayload, "test-role", "String")
        val nonRoleEncryptionResult = dataCipher.encrypt(testPayload, null, "String")

        assertTrue(roleEncryptionResult.data.size > nonRoleEncryptionResult.data.size)
    }

    @Test
    fun testEncryptionUniqueness() {
        val testPayload = "Encrypt this String".toByteArray()
        val resultOne = dataCipher.encrypt(testPayload, "test-role", "String")
        val resultTwo = dataCipher.encrypt(testPayload, "test-role", "String")

        val isEqual = Arrays.areEqual(resultOne.data, resultTwo.data)

        assertFalse(isEqual)
    }
}