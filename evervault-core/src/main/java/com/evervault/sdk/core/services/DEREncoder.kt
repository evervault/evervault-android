package com.evervault.sdk.core.services

import com.evervault.sdk.core.exceptions.Asn1EncodingException
import com.evervault.sdk.core.models.Secp256r1Constants
import com.evervault.sdk.core.utils.HexHandler

internal class DEREncoder(constants: Secp256r1Constants) {
    protected val curveValues: Secp256r1Constants

    init {
        curveValues = constants
    }

    @Throws(Asn1EncodingException::class)
    fun publicKeyToDer(decompressedPublicKey: ByteArray): ByteArray {
        val encoded = ASN1.encode(
            "30",
            ASN1.encode(
                "30",  // 1.2.840.10045.2.1 ecPublicKey
                // (ANSI X9.62 public key type)
                ASN1.encode("06", "2A 86 48 CE 3D 02 01"),
                ASN1.encode(
                    "30",  // ECParameters Version
                    ASN1.UINT("01"),
                    ASN1.encode(
                        "30",  // X9.62 Prime Field
                        ASN1.encode("06", "2A 86 48 CE 3D 01 01"),
                        ASN1.UINT(curveValues.p)
                    ),
                    ASN1.encode(
                        "30",
                        ASN1.encode("04", curveValues.a),
                        ASN1.encode("04", curveValues.b),
                        ASN1.BITSTR(curveValues.seed)
                    ),  // curve generate point in decompressed form
                    ASN1.encode("04", curveValues.generator),
                    ASN1.UINT(curveValues.n),
                    ASN1.UINT(curveValues.h)
                )
            ),
            ASN1.BITSTR(HexHandler.encode(decompressedPublicKey))
        )
        return HexHandler.decode(encoded)
    }
}
