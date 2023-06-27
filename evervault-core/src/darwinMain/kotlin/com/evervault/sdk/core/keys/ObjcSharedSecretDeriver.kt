package com.evervault.sdk.core.keys

import com.evervault.sdk.core.exceptions.Asn1EncodingException
import com.evervault.sdk.core.models.Secp256r1Constants
import com.evervault.sdk.core.services.DEREncoder
import com.evervault.sdk.objclibs.kcrypto.*
import com.evervault.sdk.toNSData
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toCValues
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Foundation.dataWithBytes
import platform.darwin.NSObject

internal class ObjcSharedSecretDeriver: SharedSecretDeriver {

    override fun deriveSharedSecret(cageKey: CageKey): GeneratedSharedKey {
        val deriver = ObjcCryptoSharedSecretDeriver()
        val wrappedKey = deriver.deriveSharedSecretWithEcdhP256KeyUncompressed(cageKey.ecdhP256KeyUncompressed, ObjcPublicKeyEncoder())
        val generatedEcdhKey: NSData = wrappedKey.generatedEcdhKey()
        val sharedKey: NSData = wrappedKey.sharedKey()
        return GeneratedSharedKey(
            generatedEcdhKey.bytes!!.readBytes(generatedEcdhKey.length.toInt()),
            sharedKey.bytes!!.readBytes(sharedKey.length.toInt())
        )
    }

}

internal class ObjcPublicKeyEncoder: NSObject(), PublicKeyEncoderProtocol {
    override fun encodePublicKeyWithDecompressedKeyData(decompressedKeyData: NSData): NSData {
        val decompressedKeyDataByteArray = decompressedKeyData.bytes!!.readBytes(decompressedKeyData.length.toInt())

        val derEncoder = DEREncoder(Secp256r1Constants())
        val encodedPublicKey: ByteArray = try {
            derEncoder.publicKeyToDer(decompressedKeyDataByteArray)
        } catch (e: Exception) {
            throw Asn1EncodingException
        }

        return encodedPublicKey.toNSData()
    }
}
