package com.evervault.sdk.core.services

import com.evervault.sdk.core.models.Secp256r1Constants
import kotlin.test.Test
import kotlin.test.assertEquals

class ASN1EncoderTest {

    val curveValues = Secp256r1Constants()

    @Test
    fun testAsn1() {

        assertEquals(ASN1.encode("04", curveValues.a), "0420ffffffff00000001000000000000000000000000fffffffffffffffffffffffc")
        assertEquals(ASN1.encode("04", curveValues.b), "04205ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b")
        assertEquals(ASN1.encode("06", "2A 86 48 CE 3D 02 01"), "06072a8648ce3d0201")
        assertEquals(ASN1.encode(
            "30",
            ASN1.encode("04", curveValues.a), // curve a value
            ASN1.encode("04", curveValues.b), // curve b value
            ASN1.BITSTR(curveValues.seed) // curve seed value
        ), "305b0420ffffffff00000001000000000000000000000000fffffffffffffffffffffffc04205ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b031500c49d360886e704936a6678e1139d26b7819f7e90")

        var encoded = ASN1.encode(
            "30",
            ASN1.encode(
                "30",
                ASN1.encode("06", "2A 86 48 CE 3D 02 01"), // 1.2.840.10045.2.1 ecPublicKey
                ASN1.encode(
                    "30",
                    ASN1.UINT("01"), // ECParameters Version
                    ASN1.encode(
                        "30",
                        ASN1.encode("06", "2A 86 48 CE 3D 01 01"), // X9.62 Prime Field
                        ASN1.UINT(curveValues.p) // curve p value
                    ),
                    ASN1.encode(
                        "30",
                        ASN1.encode("04", curveValues.a), // curve a value
                        ASN1.encode("04", curveValues.b), // curve b value
                        ASN1.BITSTR(curveValues.seed) // curve seed value
                    ),
                    ASN1.encode("04", curveValues.generator), // curve generate point in decompressed form
                    ASN1.UINT(curveValues.n), // curve n value
                    ASN1.UINT(curveValues.h) // curve h value
                )
            )
        )
        assertEquals(encoded, "308201073082010306072a8648ce3d02013081f7020101302c06072a8648ce3d0101022100ffffffff00000001000000000000000000000000ffffffffffffffffffffffff305b0420ffffffff00000001000000000000000000000000fffffffffffffffffffffffc04205ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b031500c49d360886e704936a6678e1139d26b7819f7e900441046b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c2964fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5022100ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551020101")

        val hexKey = "04083b583132ee557bbcc8272e943183737be0ea5f4689e167e76fd5dd19f8670ee82fd70f39501e2b972e5773cb6aaa0eb3d4efdf578c1dd96a945fbd19432c15"
        encoded = ASN1.encode(
            "30",
            ASN1.encode(
                "30",
                ASN1.encode("06", "2A 86 48 CE 3D 02 01"), // 1.2.840.10045.2.1 ecPublicKey
                ASN1.encode(
                    "30",
                    ASN1.UINT("01"), // ECParameters Version
                    ASN1.encode(
                        "30",
                        ASN1.encode("06", "2A 86 48 CE 3D 01 01"), // X9.62 Prime Field
                        ASN1.UINT(curveValues.p) // curve p value
                    ),
                    ASN1.encode(
                        "30",
                        ASN1.encode("04", curveValues.a), // curve a value
                        ASN1.encode("04", curveValues.b), // curve b value
                        ASN1.BITSTR(curveValues.seed) // curve seed value
                    ),
                    ASN1.encode("04", curveValues.generator), // curve generate point in decompressed form
                    ASN1.UINT(curveValues.n), // curve n value
                    ASN1.UINT(curveValues.h) // curve h value
                )
            ),
            ASN1.BITSTR(hexKey)
        )
        assertEquals(encoded, "3082014b3082010306072a8648ce3d02013081f7020101302c06072a8648ce3d0101022100ffffffff00000001000000000000000000000000ffffffffffffffffffffffff305b0420ffffffff00000001000000000000000000000000fffffffffffffffffffffffc04205ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b031500c49d360886e704936a6678e1139d26b7819f7e900441046b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c2964fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5022100ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc63255102010103420004083b583132ee557bbcc8272e943183737be0ea5f4689e167e76fd5dd19f8670ee82fd70f39501e2b972e5773cb6aaa0eb3d4efdf578c1dd96a945fbd19432c15")
    }
}
