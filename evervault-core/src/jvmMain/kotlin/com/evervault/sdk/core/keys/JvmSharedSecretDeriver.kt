package com.evervault.sdk.core.keys

import com.evervault.sdk.core.exceptions.Asn1EncodingException
import com.evervault.sdk.core.models.Secp256r1Constants
import com.evervault.sdk.core.services.DEREncoder
import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec
import javax.crypto.KeyAgreement
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class JvmSharedSecretDeriver: SharedSecretDeriver {

    private val provider = BouncyCastleProvider()

    override fun deriveSharedSecret(cageKey: CageKey): GeneratedSharedKey {
        val teamKey = getEllipticCurvePublicKeyFrom(cageKey.ecdhP256KeyUncompressed)
        return generateSharedKeyBasedOn(teamKey)
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun getEllipticCurvePublicKeyFrom(base64key: String): PublicKey {
        val publicKeyByteArray = Base64.decode(base64key)
        val spec = ECNamedCurveTable.getParameterSpec("secp256r1")
        val point = spec.curve.decodePoint(publicKeyByteArray)
        val publicKeySpec = ECPublicKeySpec(point, spec)

        val keyFactory = KeyFactory.getInstance("ECDH", provider)
        return keyFactory.generatePublic(publicKeySpec)
    }


    private fun generateNewKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("EC", provider)
        val genParameter = ECGenParameterSpec("secp256r1")
        keyPairGenerator.initialize(genParameter, SecureRandom())
        return keyPairGenerator.generateKeyPair()
    }

    private fun generateSharedKeyBasedOn(teamCagePublicKey: PublicKey): GeneratedSharedKey {
        val keyPair = generateNewKeyPair()

        val agreement: KeyAgreement = KeyAgreement.getInstance("ECDH", provider)
        agreement.init(keyPair.private)
        agreement.doPhase(teamCagePublicKey, true)

        val generatedPublicKey = (keyPair.public as BCECPublicKey).q.getEncoded(true)
        val secret = agreement.generateSecret()

        val padding = ByteBuffer.allocate(4).putInt(1).array()
        val uncompressedKey = (keyPair.public as BCECPublicKey).q.getEncoded(false)

        val derEncoder = DEREncoder(Secp256r1Constants())
        val encodedPublicKey: ByteArray = try {
            derEncoder.publicKeyToDer(uncompressedKey)
        } catch (e: Exception) {
            throw Asn1EncodingException
        }

        val concatSecret = ByteBuffer.allocate(secret.size + padding.size + encodedPublicKey.size)
            .put(secret)
            .put(padding)
            .put(encodedPublicKey)
            .array()


        val sha256 = SHA256Digest()
        val hash = ByteArray(sha256.digestSize)
        sha256.update(concatSecret, 0, concatSecret.size)
        sha256.doFinal(hash, 0)

        return GeneratedSharedKey(
            generatedEcdhKey = generatedPublicKey,
            sharedKey = hash,
        )
    }
}
